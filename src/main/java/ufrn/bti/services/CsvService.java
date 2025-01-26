package ufrn.bti.services;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
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

@Slf4j
public class CsvService {

	private final static String AGENCIA_PATH = "src/main/resources/agencia.csv";
    private final static String CLIENT_PATH = "src/main/resources/cliente.csv";
    private final static String CONTA_PATH = "src/main/resources/conta.csv";

    private final static CSVFormat FORMAT_CSV = CSVFormat.DEFAULT.builder().build();
    
    public Banco lerArquivos() {
    	
    	Banco banco = new Banco();
    	
    	List<Agencia_> agencias = lerArquivoAgencia();
    	List<Cliente> clientes = lerArquivoCliente();
    	List<Conta> contas = lerArquivoConta(agencias, clientes);
    	
    	clientes.forEach(c -> {
    		contas.forEach(co -> {
    			if(co.getUsuario().getCpf().equals(c.getCpf())) {
    				c.adicionarConta(co);
    			}
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
    
    
    public static List<Agencia_> lerArquivoAgencia() {
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
            log.error("Erro ao ler arquivo do banco", e);
        }
        
        return agencias;
    }
    
    
    public static List<Cliente> lerArquivoCliente() {
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
            log.error("Erro ao ler arquivo do banco", e);
        }
        
        return clientes;
    }
    
    
    public static List<Conta> lerArquivoConta(List<Agencia_> agencias, List<Cliente> clientes) {
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
            log.error("Erro ao ler arquivo do banco", e);
        }
        
        return contas;
    }
    
    public static Banco findBanco(Integer idBanco, List<Banco> bancos) {
    	
    	Banco banco = bancos.stream().filter(b -> b.getId().equals(idBanco))
    			.collect(Collectors.toList()).getFirst();
    	
    	return banco;
    }
    
    public static Agencia_ findAgencia(String idAgencia, List<Agencia_> agencias) {
    	
    	Agencia_ agencia = agencias.stream().filter(a -> a.getId().equals(idAgencia))
    			.collect(Collectors.toList()).getFirst();
    	
    	return agencia;
    }
    
    public static Cliente findCliente(String cpf, List<Cliente> clientes) {
    	
    	Cliente cliente = clientes.stream().filter(c -> c.getCpf().equals(cpf.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "")))
    			.collect(Collectors.toList()).getFirst();
    	
    	return cliente;
    }
    
}
