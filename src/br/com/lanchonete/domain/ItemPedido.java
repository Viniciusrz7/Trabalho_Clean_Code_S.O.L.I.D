package br.com.lanchonete.domain;

public class ItemPedido {

    private int quantidade;
    private Produto produto;

    public ItemPedido(int quantidade, Produto produto) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public double calcularSubTotal() {
        return produto.getPreco() * quantidade;
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
    }
}
