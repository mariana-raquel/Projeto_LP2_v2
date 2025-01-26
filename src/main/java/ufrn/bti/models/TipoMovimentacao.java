package ufrn.bti.models;

public enum TipoMovimentacao {
	DEPOSITO, SAQUE, TRANSFERENCIA;
	
	public static TipoMovimentacao getTipoMovimentacao(Integer numero) {
        for (TipoMovimentacao mov : TipoMovimentacao.values()) {
            if (mov.ordinal() == numero) {
                return mov;
            }
        }
        return null;
    }
}
