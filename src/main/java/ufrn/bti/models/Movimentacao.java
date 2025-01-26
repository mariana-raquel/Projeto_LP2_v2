package ufrn.bti.models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Movimentacao {
	 
	private Date data;
	private Double valor;
	private String contaOrigem;
	private String contaDestino;
	private TipoMovimentacao tipo;
	
	public Movimentacao(Date data, TipoMovimentacao tipoMovim, String contaOrigem, String contaDestino, Double valor) {
		this.data = data;
		this.tipo = tipoMovim;
		this.contaOrigem = contaOrigem;
		this.contaDestino = contaDestino;
		this.valor = valor;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public String getContaOrigem() {
		return contaOrigem;
	}
	
	public void setContaOrigem(String contaorigem) {
		this.contaOrigem = contaorigem;
	}
	
	public String getContaDestino() {
		return contaDestino;
	}
	
	public void setContaDestino(String contaDestino) {
		this.contaDestino = contaDestino;
	}
	
	public TipoMovimentacao getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoMovimentacao tipo) {
		this.tipo = tipo;
	}
	
	

	@Override
	public int hashCode() {
		return Objects.hash(contaDestino, contaOrigem, data, tipo, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movimentacao other = (Movimentacao) obj;
		return Objects.equals(contaDestino, other.contaDestino) && Objects.equals(contaOrigem, other.contaOrigem)
				&& Objects.equals(data, other.data) && tipo == other.tipo && Objects.equals(valor, other.valor);
	}

	@Override
	public String toString() {
		Locale localeBr = Locale.of("pt", "BR");
		
		return "Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data) + ", Valor: " + NumberFormat.getCurrencyInstance(localeBr).format(valor) 
			+ ", Conta Origem: " + contaOrigem + ", Conta Destino: " + contaDestino + ", Tipo: " + tipo.name();
	}
	
}
