package ufrn.bti.models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movimentacao {
	 
	private Date data;
	private Double valor;
	private String contaorigem;
	private String contaDestino;
	private TipoMovimentacao tipo;
	
	public Movimentacao(Date data, TipoMovimentacao tipoMovim, String contaOrigem, String contaDestino, Double valor) {
		this.data = data;
		this.tipo = tipoMovim;
		this.contaorigem = contaOrigem;
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
	
	public String getContaorigem() {
		return contaorigem;
	}
	
	public void setContaorigem(String contaorigem) {
		this.contaorigem = contaorigem;
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
	public String toString() {
		Locale localeBr = Locale.of("pt", "BR");
		
		return "Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data) + ", Valor: " + NumberFormat.getCurrencyInstance(localeBr).format(valor) 
			+ ", Conta Origem: " + contaorigem + ", Conta Destino: " + contaDestino + ", Tipo: " + tipo.name();
	}

	
	
}
