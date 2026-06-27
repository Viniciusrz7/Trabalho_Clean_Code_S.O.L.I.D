package br.com.lanchonete.integration;

import br.com.lanchonete.domain.StatusPedido;
import java.util.function.Consumer;

public class AdaptadorNotificacaoPush implements ObservadorNotificacaoPush {

    private final Consumer<String> disparadorNotificacao;

    public AdaptadorNotificacaoPush(Consumer<String> disparadorNotificacao) {
        this.disparadorNotificacao = disparadorNotificacao;
    }

    @Override
    public void atualizarStatusPedido(int idPedido, StatusPedido novoStatus) {
        disparadorNotificacao.accept("NOTIFICAÇÃO PUSH: Olá! O status do seu pedido #" + idPedido + " foi atualizado para: " + novoStatus.getDescricao());
    }
}
