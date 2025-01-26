package ufrn.bti.models;

public enum Agencia {
    AGENCIA1(("001")), AGENCIA2("002"), AGENCIA3("003"), AGENCIA4("004");

    private String numero;

    Agencia(String numero) {
        this.numero = numero;
    }

    public static Agencia getAgencia(String numero) {
        for (Agencia agencia : Agencia.values()) {
            if (agencia.numero.equals(numero)) {
                return agencia;
            }
        }
        return null;
    }

    public static void imprimirAgencias() {
        System.out.println("Agências disponíveis: ");
        for (Agencia agencia : Agencia.values()) {
            System.out.println(agencia.numero);
        }
    }
}
