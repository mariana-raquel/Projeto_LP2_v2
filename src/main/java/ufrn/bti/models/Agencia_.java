package ufrn.bti.models;

public class Agencia_ {

	private String id;
	private String nome;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return id + " - " + nome;
	}
	
}
