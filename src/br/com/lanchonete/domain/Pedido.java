package br.com.lanchonete.domain;

import br.com.lanchonete.payment.Pagamento;
import br.com.lanchonete.strategy.DescontoStrategy;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int id;
    private StatusPedido status;
    private List<ItemPedido> itens;
    private Pagamento pagamento;
    private DescontoStrategy descontoStrategy;

    public Pedido(int id) {
        this.id = id;
        this.status = StatusPedido.PENDENTE;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularSubTotal();
        }
        return total;
    }

    public double calcularTotalComDesconto() {
        double totalBruto = calcularTotal();
        if (descontoStrategy != null) {
            return descontoStrategy.calcularDesconto(totalBruto);
        }
        return totalBruto;
    }

    public int getId() {
        return id;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public DescontoStrategy getDescontoStrategy() {
        return descontoStrategy;
    }

    public void setDescontoStrategy(DescontoStrategy descontoStrategy) {
        this.descontoStrategy = descontoStrategy;
    }
}
