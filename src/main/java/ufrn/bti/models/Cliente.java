package ufrn.bti.models;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import ufrn.bti.exceptions.InputInvalidoException;

public class Cliente {

	private String nome;
	private String cpf;
	private String senha;
	private List<Conta> contas;
	
	public Cliente(String nome, String cpf, String senha) throws Exception {
		
		if (!StringUtils.isNotBlank(nome)) {
			throw new InputInvalidoException("Nome não pode ser vazio!");
		}

		if (!StringUtils.isNotBlank(cpf)) {
			throw new InputInvalidoException("CPF não pode ser vazio!");
		}

		if (!StringUtils.isNotBlank(senha)) {
			throw new InputInvalidoException("Senha não pode ser vazia!");
		}

		this.nome = nome;
		this.cpf = cpf;
		this.senha = senha;
		this.contas = Lists.newArrayList();
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public List<Conta> getContas() {
		return contas;
	}
	
	public void adicionarConta(Conta conta) {
		this.contas.add(conta);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return "Cliente [nome=" + nome + ", cpf=" + cpf + ", contas=" + contas + "]";
	}
	
}
