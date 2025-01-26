package ufrn.bti.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import ufrn.bti.exceptions.DepositoInvalidoException;
import ufrn.bti.exceptions.SaqueInvalidoException;
import ufrn.bti.exceptions.TransferenciaInvalidaException;
import ufrn.bti.models.Agencia;
import ufrn.bti.models.Agencia_;
import ufrn.bti.models.Banco;
import ufrn.bti.models.Cliente;
import ufrn.bti.models.Conta;
import ufrn.bti.models.ContaCorrente;
import ufrn.bti.models.ContaPoupanca;

@Slf4j
public class BancoService {

	private NumberFormat formatarValores;

	public BancoService() {
		Locale localeBr = Locale.of("pt", "BR");
		this.formatarValores = NumberFormat.getCurrencyInstance(localeBr);
	}

	public Cliente cadastrarUsuario(Scanner scan, Banco banco) {

		Cliente usuario = null;

		try {

			log.info("Informe o nome do cliente/usuário:");
			String nome = scan.nextLine();
			log.info("Informe o CPF do cliente/usuário:");
			String cpf = scan.nextLine();
			log.info("Informe a senha do cliente/usuário:");
			String senha = scan.nextLine();

			String cpf_ = cpf.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "");

			List<Cliente> filtrados = banco.getClientes().stream()
					.filter(c -> c.getCpf().equals(cpf_))
					.collect(Collectors.toList());

			if (filtrados.size() > 0) {
				log.info("Já existe um usuário cadastrado para esse CPF: {}!", cpf_);

			} else {

				usuario = new Cliente(nome, cpf_, senha);

				if (Objects.nonNull(usuario)) {

					Integer tipo = 0;
					log.info("\nVocê deseja criar uma conta corrente ou poupança?");
					log.info("1 - Corrente \t\t 2 - Poupança");
					tipo = Integer.valueOf(scan.nextLine());

					while (tipo < 1 || tipo > 2) {
						log.info("Por favor selecione uma opção válida!");
						log.info("1 - Corrente \t\t 2 - Poupança");
						tipo = Integer.valueOf(scan.nextLine());
					}

					log.info("Selecione a agência da conta:");
					Agencia.imprimirAgencias();
					Agencia agencia = null;
					while (agencia == null) {
						String agenciaCod = scan.nextLine();
						agencia = Agencia.getAgencia(agenciaCod);
						if (agencia == null) {
							log.info("Agência não encontrada! Tente novamente!");
						}
					}

					Conta conta = null;
					if (tipo == 1) {
						conta = new ContaCorrente(usuario);
					} else {
						conta = new ContaPoupanca(usuario);
					}
					conta.setAgencia(agencia);
					if (Objects.nonNull(conta)) {
						usuario.adicionarConta(conta);
					}
				}

			}

		} catch (Exception e) {
			log.error("Falha ao criar usuario!", e);
		}

		return usuario;

	}

	public Conta cadastrarNovaConta(Scanner scan, Banco banco) {

		Conta conta = null;

		try {

			if (banco.getClientes().size() > 0) {

				Cliente usuario = null;

				while (Objects.isNull(usuario)) {
					usuario = buscarUsuario(scan, banco);
				}

				if (Objects.nonNull(usuario)) {
					Integer tipo = 0;
					log.info("Você escolheu o usuário {} ({})\n", usuario.getNome(), usuario.getCpf());
					log.info("Você deseja criar uma conta corrente ou poupança?");
					log.info("1 - Corrente \t\t 2 - Poupança");
					tipo = Integer.valueOf(scan.nextLine());

					while (tipo < 1 || tipo > 2) {
						log.info("Por favor selecione uma opção válida!");
						log.info("1 - Corrente \t\t 2 - Poupança");
						tipo = Integer.valueOf(scan.nextLine());
					}

					log.info("Selecione a agência da conta:");
					imprimirAgencias(banco);
					Agencia.imprimirAgencias();
					Agencia agencia = null;
					Agencia_ agencia_ = null;
					while (agencia == null) {
						String agenciaCod = scan.nextLine();
						agencia = Agencia.getAgencia(agenciaCod);
						
						agencia_ = banco.getAgencias().stream().filter(a -> a.getId().equals(agenciaCod)).collect(Collectors.toList()).getFirst();
						
						if (agencia == null) {
							log.info("Agência não encontrada! Tente novamente!");
						}
					}

					if (tipo == 1) {
						conta = new ContaCorrente(usuario);
					} else {
						conta = new ContaPoupanca(usuario);
					}

					conta.setAgencia(agencia);
					conta.setAgencia_(agencia_);
					if (Objects.nonNull(conta)) {
						usuario.adicionarConta(conta);
					}
				}

			} else {
				log.info("Crie algum usuário antes de criar uma conta!");
			}

		} catch (Exception e) {
			log.error("Falha ao criar conta!", e);
		}

		return conta;
	}

	public void consultarSaldo(Scanner scan, Banco banco) {

		Cliente usuario = banco.getUsuarioLogado();

		log.info("De qual conta deseja consultar o saldo?");
		Conta conta = selecionarConta(scan, usuario);

		if (Objects.nonNull(conta)) {
			BigDecimal saldo = BigDecimal.valueOf(conta.getSaldo()).setScale(2, RoundingMode.HALF_UP);
			log.info("Conta {} ({}) possui o saldo de {}", conta.getNumero(), conta.getTipo().name(),
					this.formatarValores.format(saldo));
		} else {
			log.info("Falha ao selecionar conta!");
		}

	}

	public void sacar(Scanner scan, Banco banco) throws SaqueInvalidoException {

		Double valor = 0.0;
		Cliente usuario = banco.getUsuarioLogado();

		log.info("Em qual conta deseja sacar o valor?");
		Conta conta = selecionarConta(scan, usuario);

		if (Objects.nonNull(conta)) {

			BigDecimal saldoFormatado = BigDecimal.valueOf(conta.getSaldo()).setScale(2, RoundingMode.HALF_UP);

			if (conta.getSaldo() > 0) {

				log.info("Seu saldo atual é de {}", saldoFormatado);
				log.info("Qual valor deseja sacar?");
				valor = Double.valueOf(scan.nextLine());
				while (valor <= 0 || valor > conta.getSaldo()) {
					log.info("\nVocê não pode sacar um valor menor ou igual a zero, nem maior que o disponível!");
					log.info("Por favor, informe novamente qual valor deseja sacar");
					valor = Double.valueOf(scan.nextLine());
				}

				log.info("Você deseja sacar um valor de {}", this.formatarValores.format(valor));

				conta.sacar(valor);
				saldoFormatado = BigDecimal.valueOf(conta.getSaldo()).setScale(2, RoundingMode.HALF_UP);
				log.info("Seu saldo atual é de {}", this.formatarValores.format(saldoFormatado));

			} else {
				log.info("Impossível realizar saque! Seu saldo atual é de {}", saldoFormatado);
			}

		} else {
			log.info("Falha ao selecionar conta!");
		}

	}

	public void depositar(Scanner scan, Banco banco) throws DepositoInvalidoException {

		Double valor = 0.0;
		Cliente usuario = banco.getUsuarioLogado();

		log.info("Em qual conta deseja depositar o valor?");
		Conta conta = selecionarConta(scan, usuario);

		if (Objects.nonNull(conta)) {

			log.info("Qual valor deseja depositar?");
			valor = Double.valueOf(scan.nextLine());

			while (valor <= 0) {
				log.info("\nVocê não pode depositar um valor menor ou igual a zero!");
				log.info("Por favor, informe novamente qual valor deseja depositar");
				valor = Double.valueOf(scan.nextLine());
			}

			conta.depositar(valor);
			BigDecimal valorFormatado = BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
			BigDecimal saldoFormatado = BigDecimal.valueOf(conta.getSaldo()).setScale(2, RoundingMode.HALF_UP);
			log.info("\nVocê realizou um depósito de {}", this.formatarValores.format(valorFormatado));
			log.info("Seu saldo atual é de {}", this.formatarValores.format(saldoFormatado));

		} else {
			log.info("Falha ao selecionar conta!");
		}
	}

	public void transferir(Scanner scan, Banco banco) throws TransferenciaInvalidaException {

		Double valor = 0.0;
		Cliente usuario = banco.getUsuarioLogado();

		log.info("De qual conta deseja transferir o valor?");
		Conta contaOrigem = selecionarConta(scan, usuario);

		if (Objects.nonNull(contaOrigem)) {
			BigDecimal saldoFormatado = BigDecimal.valueOf(contaOrigem.getSaldo()).setScale(2, RoundingMode.HALF_UP);
			if (contaOrigem.getSaldo() > 0) {

				Conta contaDestino = selecionarContaDestino(scan, banco, contaOrigem);

				log.info("Seu saldo atual é de {}", saldoFormatado);
				log.info("Qual valor deseja transferir?");
				valor = Double.valueOf(scan.nextLine());
				while (valor <= 0 || valor > contaOrigem.getSaldo()) {
					log.info("\nVocê não pode transferir um valor menor ou igual a zero, nem maior que o disponível!");
					log.info("Por favor, informe novamente qual valor deseja transferir");
					valor = Double.valueOf(scan.nextLine());
				}
				
				contaOrigem.transferir(contaDestino, valor);
				BigDecimal valorFormatado = BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
				saldoFormatado = BigDecimal.valueOf(contaOrigem.getSaldo()).setScale(2, RoundingMode.HALF_UP);
				log.info("\nVocê realizou uma transferência de {} para conta: {} - ({} - {})",
						this.formatarValores.format(valorFormatado),
						contaDestino.getNumero(), contaDestino.getUsuario().getNome(),
						contaDestino.getUsuario().getCpf());
				log.info("Seu saldo atual é de {}", this.formatarValores.format(saldoFormatado));

			} else {
				log.info("Impossível realizar transferência! Seu saldo atual é de {}", saldoFormatado);
			}

		} else {
			log.info("Falha ao selecionar conta origem!");
		}
	}

	public Conta selecionarConta(Scanner scan, Cliente usuario) {

		List<Integer> numerosContas = Lists.newArrayList();
		Conta conta = null;
		Integer cont = 0;

		try {

			usuario.getContas().forEach(c -> {
				numerosContas.add(c.getNumero());
				log.info("{} ({})", c.getNumero(), c.getTipo().name());
			});

			cont = Integer.valueOf(scan.nextLine());
			while (!numerosContas.contains(cont)) {
				log.info("\nPor favor selecione uma conta válida!");
				cont = Integer.valueOf(scan.nextLine());
			}

			conta = recuperarConta(usuario.getContas(), cont);

		} catch (Exception e) {
			log.info("Falha ao selecionar conta!", e);
		}

		return conta;

	}

	private Conta selecionarContaDestino(Scanner scan, Banco banco, Conta contaOrigem) {
		log.info("\nPara qual conta deseja transferir o valor?");
		Conta contaDestino = null;
		Boolean valido = false;

		while (!valido) {
			contaDestino = buscarConta(scan, banco);
			if (Objects.nonNull(contaDestino)) {
				if (contaOrigem.equals(contaDestino)) {
					log.info("\nA conta destino não pode ser igual a conta de origem, por favor escolha novamente!");
				} else {
					valido = true;
				}
			} else {
				log.info("Conta não encontrada no sistema! Tente novamente!");
			}
		}
		return contaDestino;
	}

	public Cliente buscarUsuario(Scanner scan, Banco banco) {

		Cliente usuario = null;

		try {

			log.info("Informe o CPF do cliente/usuário:");
			String cpf = scan.nextLine();

			String cpf_ = cpf.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "");
			List<Cliente> filtrados = banco.getClientes().stream()
					.filter(c -> c.getCpf().equals(cpf_)).collect(Collectors.toList());

			if (filtrados.size() > 0) {
				usuario = filtrados.get(0);
			} else {
				log.info("\nUsuário com CPF {} não está cadastrado no sistema!", cpf_);
			}

		} catch (Exception e) {
			log.info("Falha ao realizar busca de usuário!", e);
		}

		return usuario;
	}

	public Conta buscarConta(Scanner scan, Banco banco) {

		List<Integer> numerosContas = Lists.newArrayList();
		Integer cont = 0;
		Conta conta = null;

		try {

			List<Conta> contas = Lists.newArrayList();

			banco.getClientes().forEach(u -> {
				u.getContas().forEach(c -> {
					contas.add(c);
					numerosContas.add(c.getNumero());
					log.info("Conta: {} ({}) - Usuário: {} ({})",
							c.getNumero(), c.getTipo().name(), c.getUsuario().getNome(), c.getUsuario().getCpf());
				});
			});

			cont = Integer.valueOf(scan.nextLine());
			while (!numerosContas.contains(cont)) {
				log.info("Por favor selecione uma conta válida!");
				cont = Integer.valueOf(scan.nextLine());
			}

			conta = recuperarConta(contas, cont);

		} catch (Exception e) {
			log.info("Falha ao selecionar conta!", e);
		}

		return conta;
	}

	public Conta recuperarConta(List<Conta> contas, Integer numero) {
		Conta conta = null;

		try {

			for (Conta c : contas) {
				if (c.getNumero().equals(numero)) {
					conta = c;
				}

			}

		} catch (Exception e) {
			log.error("Falha ao recuperar conta: {}!", numero);
		}

		return conta;
	}

	public void listarContas(Banco banco) {

		List<Conta> contas = Lists.newArrayList();

		banco.getClientes().forEach(c -> {
			contas.addAll(c.getContas());
		});

		if (contas.size() > 0) {
			log.info("Estas são as contas cadastradas no sistema:");
			contas.forEach(c -> {
				log.info("Conta: {} ({}) - Usuário: {} ({})",
						c.getNumero(), c.getTipo().name(), c.getUsuario().getNome(), c.getUsuario().getCpf());
			});
		} else {
			log.info("Não existem contas cadastradas!");
		}

	}

	public void listarUsuarios(Banco banco) {

		if (banco.getClientes().size() > 0) {
			log.info("Estas são os usuários cadastrados no sistema:");
			banco.getClientes().forEach(u -> {
				log.info("Nome: {} ({})", u.getNome(), u.getCpf());
			});
		} else {
			log.info("Não existem usuários cadastrados no sistema!");
		}

	}

	public void login(Scanner scan, Banco banco) {

		try {

			log.info("Informe o CPF do cliente/usuário:");
			String cpf = scan.nextLine();
			log.info("Informe a senha do cliente/usuário:");
			String senha = scan.nextLine();

			String cpf_ = cpf.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "");
			List<Cliente> filtrados = banco.getClientes().stream()
					.filter(c -> c.getCpf().equals(cpf_) && c.getSenha().equals(senha)).collect(Collectors.toList());

			if (filtrados.size() > 0) {
				banco.setUsuarioLogado(filtrados.get(0));
				operacoesLogadas(scan, banco);
			} else {
				log.info("\nDesculpe, não encontramos nenhum usuário com essas credenciais");
			}

		} catch (Exception e) {
			log.info("Falha ao realizar login!", e);
		}

	}

	public void operacoesLogadas(Scanner scan, Banco banco) {

		Integer opcao = 0;

		try {

			log.info("\nSeja bem-vindo(a), {}!", banco.getUsuarioLogado().getNome());
			while (opcao < 1 || opcao > 5) {

				log.info("Qual funcionalidade deseja utilizar:");
				log.info("1 - Consultar saldo");
				log.info("2 - Realizar Depósito");
				log.info("3 - Realizar Saque");
				log.info("4 - Realizar Transferência");
				log.info("5 - Realizar logout\n");

				opcao = Integer.valueOf(scan.nextLine());
				if (opcao >= 1 && opcao <= 5) {
					switch (opcao) {
						case 1:
							log.info("Você escolheu a opção: 1 - Consultar saldo\n");
							consultarSaldo(scan, banco);
							opcao = maisOpcoes(scan);
							break;
						case 2:
							log.info("Você escolheu a opção: 2 - Realizar Depósito\n");
							depositar(scan, banco);
							opcao = maisOpcoes(scan);
							break;
						case 3:
							log.info("Você escolheu a opção: 3 - Realizar Saque\n");
							sacar(scan, banco);
							opcao = maisOpcoes(scan);
							break;
						case 4:
							log.info("Você escolheu a opção: 4 - Realizar Transferência\n");
							transferir(scan, banco);
							opcao = maisOpcoes(scan);
							break;
						case 5:
							log.info("Você escolheu a opção: 5 - Realizar logout\n");
							banco.setUsuarioLogado(null);
							break;
						default:
							break;
					}
				}
			}

			log.info("Obrigado por utilizar nosso serviços!");

		} catch (Exception e) {
			log.error("Falha nas operações do {}!", banco.getNome(), e);
		}

	}

	public Integer maisOpcoes(Scanner scan) {
		Integer opcao = 0;
		try {
			log.info("\nDeseja utilizar alguma outra funcionalidade?");
			log.info("1 - Sim\t\t2 - Não");
			opcao = Integer.valueOf(scan.nextLine());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return opcao == 1 ? 0 : 5;
	}
	
	
	public void imprimirAgencias(Banco banco) {
        log.info("Agências disponíveis: ");
        for (Agencia_ agencia : banco.getAgencias()) {
            log.info("{}- {}", agencia.getId(), agencia.getNome());
        }
    }

}
