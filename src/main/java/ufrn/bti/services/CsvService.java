package ufrn.bti.services;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import ufrn.bti.models.Agencia;
import ufrn.bti.models.Agencia_;
import ufrn.bti.models.Banco;
import ufrn.bti.models.Cliente;
import ufrn.bti.models.Conta;
import ufrn.bti.models.ContaCorrente;
import ufrn.bti.models.ContaPoupanca;
import ufrn.bti.models.Movimentacao;
import ufrn.bti.models.TipoMovimentacao;

@Slf4j
public class CsvService {

	private final static String AGENCIA_PATH = "src/main/resources/agencia.csv";
    private final static String CLIENT_PATH = "src/main/resources/cliente.csv";
    private final static String CONTA_PATH = "src/main/resources/conta.csv";
    private final static String MOVIMENTACAO_PATH = "src/main/resources/movimentacao.csv";

    private final static CSVFormat FORMAT_CSV = CSVFormat.DEFAULT.builder().build();
    
    public Banco lerArquivos() {
    	
    	Banco banco = new Banco();
    	
    	List<Agencia_> agencias = lerArquivoAgencia();
    	List<Cliente> clientes = lerArquivoCliente();
    	List<Conta> contas = lerArquivoConta(agencias, clientes);
    	List<Movimentacao> movimentacoes = lerArquivoMovimentacao(); 
    	
    	clientes.forEach(c -> {
    		contas.forEach(co -> {
    			if(co.getUsuario().getCpf().replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "")
    					.equals(c.getCpf().replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", ""))) {
    				c.adicionarConta(co);
    			}
    			movimentacoes.forEach(m -> {
    				if(m.getContaOrigem().equals(co.getNumero().toString())) {
    					if(!co.getMovimentacoes().contains(m)) {
    						co.adicionarMovimentacoes(m);    						
    					}
    				}
    			});
    		});
    	});
    	
    	banco.setClientes(clientes);
    	banco.setAgencias(agencias);
    	
    	return banco;
    }
    
    
    public void listarInformacoes(Banco banco) {
    	
    	log.info("Clientes:");
    	banco.getClientes().forEach(c -> {
    		log.info("{} - {} - {}", c.getNome(), c.getCpf(), c.getSenha());
    		
    		for (Conta conta : c.getContas()) {
    			log.info("{}\n", conta);
    		}
    	});
    	
    }
    
    
    private List<Agencia_> lerArquivoAgencia() {
    	List<Agencia_> agencias = Lists.newArrayList();
    	
        try (Reader in = new FileReader(AGENCIA_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	Agencia_ agencia = new Agencia_();
            	agencia.setId(record.get(0));
            	agencia.setNome(record.get(1));
            	agencias.add(agencia);
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivo das agencias", e);
        }
        
        return agencias;
    }
    
    
    private List<Cliente> lerArquivoCliente() {
    	List<Cliente> clientes = Lists.newArrayList();
    	
        try (Reader in = new FileReader(CLIENT_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	Cliente cliente = null;
            	String nome = record.get(0);
            	String cpf = record.get(1);
            	String senha = record.get(2);
            	
            	try {
					
            		cliente = new Cliente(nome, cpf.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", ""), senha);
            		clientes.add(cliente);
            		
				} catch (Exception e) {
					log.error("Falha ao criar cliente!", e);
				}	
            }
            
        } catch (IOException e) {
            log.error("Erro ao ler arquivo dos clientes", e);
        }
        
        return clientes;
    }
    
    
    private List<Conta> lerArquivoConta(List<Agencia_> agencias, List<Cliente> clientes) {
    	List<Conta> contas = Lists.newArrayList();
    	
        try (Reader in = new FileReader(CONTA_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	Conta conta = null;
            	
            	String tipo = record.get(3);
            	
            	if(tipo.equals("1")) {
            		conta = new ContaCorrente();
            	} else {
            		conta = new ContaPoupanca();
            	}
            	
            	conta.setNumero(Integer.valueOf(record.get(0)));
            	conta.setAgencia(Agencia.getAgencia(record.get(1)));
            	conta.setAgencia_(findAgencia(record.get(1), agencias));
            	conta.setUsuario(findCliente(record.get(2), clientes));            		            	
            	conta.setSaldo(Double.valueOf(record.get(4)));
            	
            	contas.add(conta);
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivo das contas", e);
        }
        
        return contas;
    }
    
    private List<Movimentacao> lerArquivoMovimentacao() {
    	List<Movimentacao> movimentacoes = Lists.newArrayList();
    	
        try (Reader in = new FileReader(MOVIMENTACAO_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	try {
            		Date data = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(record.get(0));
            		TipoMovimentacao tipo = TipoMovimentacao.getTipoMovimentacao(Integer.valueOf(record.get(1)));
            		Double valor = Double.valueOf(record.get(4));
            		
            		Movimentacao mov = new Movimentacao(data, tipo, record.get(2), record.get(3), valor);
            		movimentacoes.add(mov);
					
				} catch (Exception e) {
					log.info("Falha ao montar movimentação", e);
				}
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivo de movimentações", e);
        }
        
        return movimentacoes;
    }
    
    
    private Agencia_ findAgencia(String idAgencia, List<Agencia_> agencias) {
    	
    	Agencia_ agencia = agencias.stream().filter(a -> a.getId().equals(idAgencia))
    			.collect(Collectors.toList()).getFirst();
    	
    	return agencia;
    }
    
    private Cliente findCliente(String cpf, List<Cliente> clientes) {
    	
    	Cliente cliente = clientes.stream().filter(c -> 
    		c.getCpf().replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "")
    			.equals(cpf.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "")))
    			.collect(Collectors.toList()).getFirst();
    	
    	return cliente;
    }
    
    
    public void salvarArquivos(Banco banco) {
    	
    	salvarArquivoAgencia(banco.getAgencias());
    	salvarArquivoCliente(banco.getClientes());
    	salvarArquivoContas(banco.getClientes());
    	salvarArquivoMovimentacao(banco.getClientes());    	
    }
    
    
    private void salvarArquivoAgencia(List<Agencia_> agencias) {
        
    	try {
			
    		Writer writer = Files.newBufferedWriter(Paths.get(AGENCIA_PATH));
    		
    		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    			
    			for (Agencia_ a : agencias) {
    				csvPrinter.printRecord(a.getId(), a.getNome());
    				csvPrinter.flush();
    			}
    			
    		} catch (Exception e) {
    			log.error("Falha escrevendo CSV", e);
    		}
    		
    		writer.close();
    		
		} catch (Exception e) {
			log.error("Falha ao salvar arquivo de agencias", e);
		}
        
    }
    
    private void salvarArquivoCliente(List<Cliente> clientes) {
        
    	try {
			
    		Writer writer = Files.newBufferedWriter(Paths.get(CLIENT_PATH));
    		
    		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    			
    			for (Cliente c: clientes) {
    				csvPrinter.printRecord(c.getNome(), c.getCpf(), c.getSenha());
    				csvPrinter.flush();
    			}
    			
    		} catch (Exception e) {
    			log.error("Falha escrevendo CSV", e);
    		}
    		
    		writer.close();
    		
		} catch (Exception e) {
			log.error("Falha ao salvar arquivo de clientes", e);
		}
           
    }
    
    private void salvarArquivoContas(List<Cliente> clientes) {
        
    	try {
			
    		Writer writer = Files.newBufferedWriter(Paths.get(CONTA_PATH));
    		
    		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    			
    			for(Cliente cl : clientes) {
    				for (Conta c: cl.getContas()) {
    					csvPrinter.printRecord(c.getNumero(), c.getAgencia_().getId(), c.getUsuario().getCpf(), c.getTipo().ordinal(), 
    							BigDecimal.valueOf(c.getSaldo()).setScale(2, RoundingMode.HALF_UP));
    					csvPrinter.flush();
    				}
    			}
    			
    		} catch (Exception e) {
    			log.error("Falha escrevendo CSV", e);
    		}
    		
    		writer.close();
    		
		} catch (Exception e) {
			log.error("Falha ao salvar arquivo de contas", e);
		}
        
    }
    
    private void salvarArquivoMovimentacao(List<Cliente> clientes) {
        
    	try {
			
    		Writer writer = Files.newBufferedWriter(Paths.get(MOVIMENTACAO_PATH));
    		
    		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    			
    			for(Cliente cl : clientes) {
    				for (Conta c: cl.getContas()) {
    					for (Movimentacao m : c.getMovimentacoes()) {
    						csvPrinter.printRecord(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.getData()), 
	    						m.getTipo().ordinal(), m.getContaOrigem(),m.getContaDestino(), 
	    						BigDecimal.valueOf(m.getValor()).setScale(2, RoundingMode.HALF_UP));
    						csvPrinter.flush();
    					}
    				}
    			}
    			
    		} catch (Exception e) {
    			log.error("Falha escrevendo CSV", e);
    		}
    		
    		writer.close();
    		
		} catch (Exception e) {
			log.error("Falha ao salvar arquivo de movimentação", e);
		}
        
        
    }
}
