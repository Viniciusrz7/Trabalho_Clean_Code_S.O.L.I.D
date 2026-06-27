package br.com.lanchonete.adapter;

import br.com.lanchonete.payment.Pagamento;

public interface GatewayPagamento {
    boolean pagar(Pagamento pagamento);
}
