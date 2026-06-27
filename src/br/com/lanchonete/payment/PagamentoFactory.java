package br.com.lanchonete.payment;

public class PagamentoFactory {

    public Pagamento criarPagamento(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de pagamento não pode ser nulo.");
        }

        switch (tipo.toUpperCase()) {
            case "PIX":
                return new PagamentoPix();
            case "CARTAO":
                return new PagamentoCartao();
            default:
                throw new IllegalArgumentException("Tipo de pagamento inválido: " + tipo);
        }
    }
}
