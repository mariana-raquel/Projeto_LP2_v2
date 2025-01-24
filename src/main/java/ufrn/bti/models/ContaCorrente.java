package ufrn.bti.models;

import ufrn.bti.exceptions.UsuarioInvalidoException;

public class ContaCorrente extends Conta {
    
    public ContaCorrente(Cliente usuario) throws UsuarioInvalidoException {
        super(usuario);
        super.setTaxaSaque(0.01);
        super.setTaxaTransferencia(0.005);
        super.setTipo(TipoConta.CORRENTE);
    }

}
