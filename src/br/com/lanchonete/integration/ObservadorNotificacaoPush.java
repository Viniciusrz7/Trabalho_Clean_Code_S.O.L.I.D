package br.com.lanchonete.integration;

import br.com.lanchonete.domain.StatusPedido;

public interface ObservadorNotificacaoPush {
    void atualizarStatusPedido(int idPedido, StatusPedido novoStatus);
}
