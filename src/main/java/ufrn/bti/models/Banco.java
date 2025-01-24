package ufrn.bti.models;

import java.util.List;

import com.google.common.collect.Lists;

public class Banco {

	private Integer id;
	private String nome;
	private Cliente usuarioLogado;
	private List<Cliente> clientes;

	public Banco() {
		this.clientes = Lists.newArrayList();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cliente getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Cliente usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public List<Cliente> getClientes() {
        return clientes;
    }

    public void adicionarCliente(Cliente cliente) {
        this.clientes.add(cliente);
    }

}
