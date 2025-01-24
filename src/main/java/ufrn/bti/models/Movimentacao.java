package ufrn.bti.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Movimentacao {
	 
	private Date data;
	private String descricao;
	private Double valor;
	private TipoMovimentacao tipo;
	
	public Movimentacao(String descricao, Double valor) {
		this.data = new Date();
		this.descricao = descricao;
		this.valor = valor;
		
		if(valor > 0) {
			this.tipo = TipoMovimentacao.CREDITO;
		} else if(valor < 0) {
			this.tipo = TipoMovimentacao.DEBITO;
		} else {
			this.tipo = TipoMovimentacao.OUTRO;
		}
	}

	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public TipoMovimentacao getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoMovimentacao tipo) {
		this.tipo = tipo;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		String dt = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
		return "Movimentacao [data=" + dt + ", descricao=" + descricao + ", tipo=" + tipo + ", valor=" + valor + "]";
	}
	
}
