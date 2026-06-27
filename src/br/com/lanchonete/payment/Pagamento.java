package br.com.lanchonete.payment;

public abstract class Pagamento {
    protected double valor;

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public abstract void processar();
    
}
