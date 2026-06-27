package br.com.lanchonete.strategy;

public class DescontoPromocional implements DescontoStrategy {

    private final double percentualDesconto;

    public DescontoPromocional(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    @Override
    public double calcularDesconto(double valorOriginal) {
        return valorOriginal - (valorOriginal * percentualDesconto);
    }
}
