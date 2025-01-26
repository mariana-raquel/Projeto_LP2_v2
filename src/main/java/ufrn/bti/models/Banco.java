package ufrn.bti.models;

import java.util.List;

import com.google.common.collect.Lists;

public class Banco {

	private Integer id;
	private String nome;
	private Cliente usuarioLogado;
	private List<Cliente> clientes;
	private List<Agencia_> agencias;

	public Banco() {
		this.nome = "Banco Azul";
		this.clientes = Lists.newArrayList();
		this.agencias = Lists.newArrayList();
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

	public Cliente getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Cliente usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public void adicionarCliente(Cliente cliente) {
        this.clientes.add(cliente);
    }

	public List<Agencia_> getAgencias() {
		return agencias;
	}

	public void setAgencias(List<Agencia_> agencias) {
		this.agencias = agencias;
	}

	@Override
	public String toString() {
		return "Banco [id=" + id + ", nome=" + nome + ", usuarioLogado=" + usuarioLogado + ", clientes=" + clientes  + "]";
	}

    
}
