package br.com.lanchonete.strategy;


public class DescontoNormal implements DescontoStrategy {

    @Override
    public double calcularDesconto(double valorOriginal) {
        return valorOriginal; 
    }
}
