package br.com.lanchonete.adapter;

import br.com.lanchonete.payment.Pagamento;
import java.util.UUID;

public class GatewayPagamentoAdapter implements GatewayPagamento {

    private GatewayExterno gatewayExterno;

    public GatewayPagamentoAdapter(GatewayExterno gatewayExterno) {
        this.gatewayExterno = gatewayExterno;
    }

    @Override
    public boolean pagar(Pagamento pagamento) {
        String txId = UUID.randomUUID().toString(); 
        gatewayExterno.processarPagamento(pagamento.getValor(), txId);
        return true;
    }
}
