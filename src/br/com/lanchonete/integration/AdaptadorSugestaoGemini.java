package br.com.lanchonete.integration;

import java.util.Arrays;
import br.com.lanchonete.domain.Pedido;
import br.com.lanchonete.domain.Produto;

public class AdaptadorSugestaoGemini implements ServicoSugestaoIA {

    @Override
    public String sugerirLanche(Pedido pedidoAtual) {
        if (!contemPalavraChave(pedidoAtual, "coca", "suco", "refrigerante")) {
            return "Sugestão da IA (Gemini): Que tal adicionar um refrigerante ou um suco para acompanhar seu pedido?";
        }
        if (!contemPalavraChave(pedidoAtual, "sorvete", "sobremesa", "doce")) {
            return "Sugestão da IA (Gemini): Recomendamos uma sobremesa para seu pedido!";
        } 
        return "Sugestão da IA (Gemini): Seu pedido está perfeito e muito bem equilibrado.";
    }

    private boolean contemPalavraChave(Pedido pedido, String... palavras) {
        return pedido.getItens().stream()
                .anyMatch(item -> produtoCorresponde(item.getProduto(), palavras));
    }

    private boolean produtoCorresponde(Produto produto, String... palavras) {
        String nomeProduto = produto.getNome().toLowerCase();
        return Arrays.stream(palavras)
                .anyMatch(palavra -> nomeProduto.contains(palavra));
    }
}
