package br.com.lanchonete.integration;

import br.com.lanchonete.domain.Pedido;

public interface ServicoSugestaoIA {
    String sugerirLanche(Pedido pedidoAtual);
}
