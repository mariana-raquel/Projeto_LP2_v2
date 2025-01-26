package ufrn.bti.bancoAzul;

import java.util.Objects;
import java.util.Scanner;


import lombok.extern.slf4j.Slf4j;
import ufrn.bti.models.Banco;
import ufrn.bti.models.Cliente;
import ufrn.bti.models.Conta;
import ufrn.bti.services.BancoService;
import ufrn.bti.services.CsvService;

@Slf4j
public class BancoAzul {

	private static final Integer MIN_OPCAO = 1;
	private static final Integer MAX_OPCAO = 6;
	

	public static void main(String[] args) {
		
		BancoService bancoService = new BancoService();
		CsvService csvService = new CsvService();
		Banco banco = csvService.lerArquivos();
		csvService.listarInformacoes(banco);
		
		Integer opcao = 0;
		
		try(Scanner scan = new Scanner(System.in);) {
			log.info("Bem vindo ao {}!", banco.getNome());
			while(opcao < MIN_OPCAO || opcao > MAX_OPCAO) {
			
				log.info("\nQual funcionalidade deseja utilizar:");
				log.info("1 - Cadastrar um cliente");
				log.info("2 - Cadastrar nova uma conta");
				log.info("3 - Listar clientes cadastrados");
				log.info("4 - Listar contas cadastradas");
				log.info("5 - Realizar login");
				log.info("6 - Sair\n");
			
				opcao = Integer.valueOf(scan.nextLine());
				if(opcao >= MIN_OPCAO && opcao <= MAX_OPCAO) {
					switch (opcao) {
						case 1:
							log.info("Você escolheu a opção: 1 - Cadastrar um cliente\n");
							Cliente usuario = bancoService.cadastrarUsuario(scan, banco);
							if(Objects.nonNull(usuario)) {


								log.info("Usuário criado com sucesso!");
								banco.adicionarCliente(usuario);
							}
							opcao = maisOpcoes(scan);
							break;
						case 2:
							log.info("Você escolheu a opção: 2 - Cadastrar nova uma conta\n");
							Conta conta = bancoService.cadastrarNovaConta(scan, banco);
							if(Objects.nonNull(conta)) {
								log.info("Conta criada com sucesso para o usuário {} ({})!", 
									conta.getUsuario().getNome(), conta.getUsuario().getCpf());
							}
							opcao = maisOpcoes(scan);
							break;
						case 3:
							log.info("Você escolheu a opção: 3 - Listar clientes cadastrados\n");
							bancoService.listarUsuarios(banco);
							opcao = maisOpcoes(scan);
							break;
						case 4:
							log.info("Você escolheu a opção: 4 - Listar contas cadastradas\n");
							bancoService.listarContas(banco);
							opcao = maisOpcoes(scan);
							break;
						case 5:
							log.info("Você escolheu a opção: 5 - Realizar login\n");
							bancoService.login(scan, banco);
							opcao = 0;
							break;
						case 6:
							log.info("Você escolheu a opção: 6 - Sair\n");
							break;
						default:
							break;
					}
				}
			}
			
			log.info("Obrigado por utilizar nosso serviços!");
			
		} catch(Exception e) {
			log.error("Falha nas operações do {}!", banco.getNome(), e);
		} finally {
			// TODO: Finalização - dumpar csv
		}
		
	}
	
	
	public static Integer maisOpcoes(Scanner scan) {
	    Integer opcao = 0;
	    try {
	    	log.info("\nDeseja utilizar alguma outra funcionalidade?");
	    	log.info("1 - Sim\t\t2 - Não");
	    	opcao = Integer.valueOf(scan.nextLine());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return opcao == 1 ? 0 : MAX_OPCAO;
	}
	
}
