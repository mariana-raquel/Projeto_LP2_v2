package ufrn.bti.models;

public class Agencia_ {

	private String id;
	private Banco banco;
		
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Banco getBanco() {
		return banco;
	}
	
	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Override
	public String toString() {
		return "Agencia_ [id=" + id + ", banco=" + banco + "]";
	}
	
}
