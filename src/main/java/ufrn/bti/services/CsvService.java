package ufrn.bti.services;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import ufrn.bti.models.Agencia;
import ufrn.bti.models.Agencia_;
import ufrn.bti.models.Banco;

@Slf4j
public class CsvService {

	private final static String AGENCIA_PATH = "src/main/resources/agencia.csv";
    private final static String BANCO_PATH = "src/main/resources/banco.csv";
    private final static String CLIENT_PATH = "src/main/resources/cliente.csv";
    private final static String CONTA_PATH = "src/main/resources/conta.csv";

    private final static CSVFormat FORMAT_CSV = CSVFormat.DEFAULT.builder().build();
    
    
    public static void main(String[] args) {
		
    	List<Banco> bancos = lerArquivoBanco();
    	List<Agencia_> agencias = lerArquivoAgencia(bancos);
    	
    	for (Banco banco : bancos) {
			log.info("{} - {}", banco.getId(), banco.getNome());
		}
    	
    	for (Agencia_ agencia : agencias) {
    		log.info("{} - {}", agencia.getId(), agencia.getBanco().getNome());
		}
	}
    
 
    public static List<Banco> lerArquivoBanco() {
    	List<Banco> bancos = Lists.newArrayList();
    	
        try (Reader in = new FileReader(BANCO_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	Banco banco = new Banco();
            	banco.setId(Integer.valueOf(record.get(0)));
            	banco.setNome(record.get(1));
            	bancos.add(banco);
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivo do banco", e);
        }
        
        return bancos;
    }
    
    public static Banco findBanco(Integer idBanco, List<Banco> bancos) {
    	
    	Banco banco = bancos.stream().filter(b -> b.getId().equals(idBanco))
    			.collect(Collectors.toList()).getFirst();
    	
    	return banco;
    }
    
    public static List<Agencia_> lerArquivoAgencia(List<Banco> bancos) {
    	List<Agencia_> agencias = Lists.newArrayList();
    	
        try (Reader in = new FileReader(AGENCIA_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	Agencia_ agencia = new Agencia_();
            	agencia.setId(record.get(0));
            	agencia.setBanco(findBanco(Integer.valueOf(record.get(1)), bancos));
            	agencias.add(agencia);
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivo do banco", e);
        }
        
        return agencias;
    }
    
    
    public static List<Banco> lerArquivoCliente() {
    	List<Banco> bancos = Lists.newArrayList();
    	
        try (Reader in = new FileReader(CLIENT_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	Banco banco = new Banco();
            	banco.setId(Integer.valueOf(record.get(0)));
            	banco.setNome(record.get(1));
            	bancos.add(banco);
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivo do banco", e);
        }
        
        return bancos;
    }
    
    
    public static List<Banco> lerArquivoConta() {
    	List<Banco> bancos = Lists.newArrayList();
    	
        try (Reader in = new FileReader(CONTA_PATH)) {
            Iterable<CSVRecord> records = FORMAT_CSV.parse(in);
            for (CSVRecord record : records) {
            	Banco banco = new Banco();
            	banco.setId(Integer.valueOf(record.get(0)));
            	banco.setNome(record.get(1));
            	bancos.add(banco);
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivo do banco", e);
        }
        
        return bancos;
    }
    
    
}
