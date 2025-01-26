package ufrn.bti.models;

import ufrn.bti.exceptions.UsuarioInvalidoException;

public class ContaPoupanca extends Conta {
    
    public ContaPoupanca() {
        super.setTaxaSaque(0.02);
        super.setTaxaTransferencia(0.01);
        super.setTipo(TipoConta.POUPANCA);
    }
	
    public ContaPoupanca(Cliente usuario) throws UsuarioInvalidoException {
        super(usuario);
        super.setTaxaSaque(0.02);
        super.setTaxaTransferencia(0.01);
        super.setTipo(TipoConta.POUPANCA);
    }
    
}
