package br.com.enonnemacher.exception;

public enum TipoDominioException {

    USUARIO("Usuario"),
    EMAIL("E-Mail"),
    CPF("CPF"),
    Id("Id"),
    Imovel("Imovel"),
    Anuncio("Anuncio"),
    IdImovel("IdImovel"),
    Pousada("Pousada"),
    Hotel("Hotel"),
    Pessoa("pessoas"),
    Diaria("diárias"),
    Pagamento("pagamento"),
    Cancelamento("cancelamento"),
    Estorno("estorno"),
    Pago("PAGO"),
    Pendente("PENDENTE"),
    Reserva("Reserva");

    private final String tipo;

    TipoDominioException(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
