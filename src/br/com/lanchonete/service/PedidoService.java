package br.com.lanchonete.service;

import br.com.lanchonete.adapter.GatewayPagamento;
import br.com.lanchonete.domain.Pedido;
import br.com.lanchonete.domain.StatusPedido;
import br.com.lanchonete.integration.ServicoSugestaoIA;
import br.com.lanchonete.integration.ObservadorNotificacaoPush;
import br.com.lanchonete.payment.Pagamento;
import java.util.ArrayList;
import java.util.List;


public class PedidoService {

    private final GatewayPagamento gatewayPagamento;
    private final ServicoSugestaoIA servicoSugestaoIA;
    private final List<ObservadorNotificacaoPush> observadores;

    public PedidoService(GatewayPagamento gatewayPagamento, ServicoSugestaoIA servicoSugestaoIA) {
        this.gatewayPagamento = gatewayPagamento;
        this.servicoSugestaoIA = servicoSugestaoIA;
        this.observadores = new ArrayList<>();
    }

    public void registrarObservador(ObservadorNotificacaoPush observer) {
        observadores.add(observer);
    }

    public String processarPedido(Pedido pedido) {
        prepararValorDoPagamento(pedido);
        realizarPagamento(pedido);
        return fornecerSugestaoIA(pedido);
    }

    private void prepararValorDoPagamento(Pedido pedido) {
        double valorFinal = pedido.calcularTotalComDesconto();
        if (pedido.getPagamento() != null) {
            pedido.getPagamento().setValor(valorFinal);
        }
    }

    private void realizarPagamento(Pedido pedido) {
        Pagamento pagamento = pedido.getPagamento();
        validarFormaDePagamento(pagamento);
        pagamento.processar();
        boolean sucesso = gatewayPagamento.pagar(pagamento);
        pedido.setStatus(sucesso ? StatusPedido.PAGO_E_FINALIZADO : StatusPedido.PAGAMENTO_RECUSADO);
        notificarObservadores(pedido);
    }

    private String fornecerSugestaoIA(Pedido pedido) {
        if (servicoSugestaoIA != null) {
            return servicoSugestaoIA.sugerirLanche(pedido);
        }
        return "";
    }

    private void validarFormaDePagamento(Pagamento pagamento){
         if (pagamento == null) {
            throw new IllegalStateException("Forma de pagamento não definida.");
        }
    }

    private void notificarObservadores(Pedido pedido) {
        observadores.forEach(observer -> 
            observer.atualizarStatusPedido(pedido.getId(), pedido.getStatus())
        );
    }
}
