package ufrn.bti.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import ufrn.bti.exceptions.DepositoInvalidoException;
import ufrn.bti.exceptions.SaqueInvalidoException;
import ufrn.bti.exceptions.TransferenciaInvalidaException;
import ufrn.bti.exceptions.UsuarioInvalidoException;
@Slf4j
public class Conta {

	private Integer numero;
	private Double saldo;
	private Cliente usuario;
	private List<Movimentacao> movimentacoes;
	private Agencia agencia;
	private Agencia_ agencia_;
	private TipoConta tipoConta;

	private Double taxaTransferencia;
	private Double taxaSaque;
	
	private NumberFormat formatarValores;
	
	public Conta() {
		Locale localeBr = Locale.of("pt", "BR");
		this.formatarValores = NumberFormat.getCurrencyInstance(localeBr);
		this.saldo = 0.00;
		this.movimentacoes = Lists.newArrayList();
	}
	
	public Conta(Cliente usuario) throws UsuarioInvalidoException {
		if (Objects.isNull(usuario.getNome()) || usuario.getNome().isEmpty()) {
			throw new UsuarioInvalidoException("Nome do usuário não pode ser vazio!");
		}
		Locale localeBr = Locale.of("pt", "BR");
		this.formatarValores = NumberFormat.getCurrencyInstance(localeBr);
		this.numero = new Random().nextInt(1000, 10000);
		this.usuario = usuario;
		this.saldo = 0.00;
		this.movimentacoes = Lists.newArrayList();
	}
	
	public void depositar(Double valor) throws DepositoInvalidoException {
		if (valor <= 0) {
			throw new DepositoInvalidoException("Você não pode depositar um valor menor ou igual a zero!");
		}
		this.saldo += valor;
		this.movimentacoes.add(new Movimentacao("DEPOSITO", valor));
	}
	
	public void sacar(Double valor) throws SaqueInvalidoException {
		if (valor <= 0) {
			throw new SaqueInvalidoException("Você não pode sacar um valor menor ou igual a zero!");
		}

		if (valor > saldo) {
			throw new SaqueInvalidoException("Você não pode sacar um valor maior do que o disponível em sua conta!");
		}

		log.info("A taxa de saque é de: R$ {}", this.taxaSaque);
		Double saque = valor + (valor * this.taxaSaque);
		BigDecimal valorFormatado = BigDecimal.valueOf(saque).setScale(2, RoundingMode.HALF_UP);
		log.info("\nVocê realizou um saque de {}", this.formatarValores.format(valorFormatado));
		this.saldo -= saque;
		this.movimentacoes.add(new Movimentacao("SAQUE", saque * -1));
	}
	
	
	public void transferir(Conta conta, Double valor) throws TransferenciaInvalidaException {
		if (valor <= 0) {
			throw new TransferenciaInvalidaException("Você não pode transferir um valor menor ou igual a zero!");
		}

		if (valor > saldo) {
			throw new TransferenciaInvalidaException("Você não pode transferir um valor maior do que o disponível em sua conta!");
		}
		
		log.info("A taxa de transferência é de: R$ {}", this.taxaTransferencia);

		conta.saldo = conta.saldo + valor;
		this.saldo -= valor + (valor * this.taxaTransferencia);
		
		this.movimentacoes.add(
			new Movimentacao(String.format("SAIDA POR TRANSFERENCIA PARA CONTA %s - %s",  conta.numero, conta.getUsuario().getNome()), valor * -1));
		conta.movimentacoes.add(
			new Movimentacao(String.format("ENTRADA POR TRANSFERENCIA DA CONTA %s - %s", this.numero, this.usuario.getNome()), valor));
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public Integer getNumero() {
		return numero;
	}

	public Double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public Cliente getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Cliente usuario) {
		this.usuario = usuario;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public Agencia getAgencia() {
		return agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}
	
	public Agencia_ getAgencia_() {
		return agencia_;
	}

	public void setAgencia_(Agencia_ agencia) {
		this.agencia_ = agencia;
	}

	public void setTaxaTransferencia(Double taxaTransferencia) {
		this.taxaTransferencia = taxaTransferencia;
	}

	public Double getTaxaTransferencia() {
		return taxaTransferencia;
	}
	
	public void setTaxaSaque(Double taxaSaque) {
		this.taxaSaque = taxaSaque;
	}

	public Double getTaxaSaque() {
		return taxaSaque;
	}
	
	public TipoConta getTipo() {
		return tipoConta;
	}

	public void setTipo(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conta other = (Conta) obj;
		return Objects.equals(numero, other.numero) && Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		BigDecimal saldoFormatado = BigDecimal.valueOf(saldo).setScale(2, RoundingMode.HALF_UP);
		return "Numero: " + numero + ", Saldo: R$ " + saldoFormatado + ", Agencia: " + agencia + ", Agencia_: " + agencia_ + ", Tipo: " + tipoConta;
	}
	
}
