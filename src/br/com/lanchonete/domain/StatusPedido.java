package br.com.lanchonete.domain;


public enum StatusPedido {
    PENDENTE("Pendente"),
    PAGO_E_FINALIZADO("Pago e Finalizado"),
    PAGAMENTO_RECUSADO("Pagamento Recusado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
