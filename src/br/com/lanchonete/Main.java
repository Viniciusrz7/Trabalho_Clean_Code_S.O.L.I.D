package br.com.lanchonete;

import br.com.lanchonete.adapter.GatewayExterno;
import br.com.lanchonete.adapter.GatewayPagamento;
import br.com.lanchonete.adapter.GatewayPagamentoAdapter;
import br.com.lanchonete.domain.ItemPedido;
import br.com.lanchonete.domain.Pedido;
import br.com.lanchonete.domain.Produto;
import br.com.lanchonete.integration.ServicoSugestaoIA;
import br.com.lanchonete.integration.AdaptadorSugestaoGemini;
import br.com.lanchonete.integration.AdaptadorNotificacaoPush;
import br.com.lanchonete.payment.PagamentoFactory;
import br.com.lanchonete.service.PedidoService;
import br.com.lanchonete.strategy.DescontoPromocional;
import br.com.lanchonete.strategy.DescontoStrategy;

import java.util.function.Consumer;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    
    public static void main(String[] args) {
        
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### SISTEMA DE GERENCIAMENTO DE PEDIDOS PARA LANCHONETE ###\n");
            
            Pedido pedido = montarPedidoInterativo(scanner);
            imprimirResumoPedido(pedido);

            String sugestaoIA = processarPedido(pedido);
            System.out.println("\n" + sugestaoIA);
            System.out.println("\n### FLUXO CONCLUÍDO ###");
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\nERRO DE NEGÓCIO: " + e.getMessage());
            System.out.println("### FLUXO INTERROMPIDO ###");
        } catch (Exception e) {
            System.out.println("\nERRO: Ocorreu um erro inesperado no sistema.");
        }
    }

   private static final Map<Integer, Produto> CARDAPIO = new TreeMap<>();
    static {
        CARDAPIO.put(1, new Produto(1, "Hambúrguer Clássico", 25.0));
        CARDAPIO.put(2, new Produto(2, "Batata Frita", 10.0));
        CARDAPIO.put(3, new Produto(3, "Coca-Cola", 8.0));
        CARDAPIO.put(4, new Produto(4, "Sorvete", 12.0));
    }
    
    private static Pedido montarPedidoInterativo(Scanner scanner) {
        Pedido pedido = new Pedido(1001);
        adicionarItensInterativo(pedido, scanner);
        aplicarPromocaoEm(pedido);
        configurarPagamentoInterativo(pedido, scanner);
        return pedido;
    }
    
    private static void adicionarItensInterativo(Pedido pedido, Scanner scanner) {
        while (true) {
            exibirCardapio();
            int opcao = scanner.nextInt();
            if (opcao == 0) {
                break;
            }
            System.out.print("Digite a quantidade: ");
            int qtd = scanner.nextInt();
            processarEscolhaProduto(opcao, qtd, pedido);
        }
    }

    private static void aplicarPromocaoEm(Pedido pedido) {
        DescontoStrategy desconto = new DescontoPromocional(0.10);
        pedido.setDescontoStrategy(desconto);
    }

    private static void configurarPagamentoInterativo(Pedido pedido, Scanner scanner) {
        System.out.println("\n### PAGAMENTO ###");
        System.out.println("1. PIX");
        System.out.println("2. CARTAO");
        System.out.print("Escolha a forma de pagamento: ");

        int opcao = scanner.nextInt();
        String tipoPagamento = (opcao == 1) ? "PIX" : "CARTAO";
        pedido.setPagamento(new PagamentoFactory().criarPagamento(tipoPagamento));
    }

    private static void exibirCardapio() {
        System.out.println("\n### CARDÁPIO ###");
        CARDAPIO.forEach((id, produto) -> {
            System.out.println(id + ". " + produto.getNome() + " (R$ " + produto.getPreco() + "0)");
        });
        System.out.println("0. Finalizar Pedido");
        System.out.print("Escolha o produto: ");
    }

    private static void processarEscolhaProduto(int opcao, int qtd, Pedido pedido) {
        Produto produto = CARDAPIO.get(opcao);
        if (produto == null) {
            System.out.println("Opção inválida, tente novamente.");
            return;
        }
        pedido.adicionarItem(new ItemPedido(qtd, produto)); 
    }

    private static void imprimirResumoPedido(Pedido pedido) {
        System.out.println("Itens no pedido: ");
        pedido.getItens().forEach(item -> 
            System.out.printf("- %dx %s\n", item.getQuantidade(), item.getProduto().getNome())
        );
        System.out.printf("Total sem desconto: R$ %.2f\n\n", pedido.calcularTotal());
    }

    private static String processarPedido(Pedido pedido) {
        PedidoService pedidoService = configurarPedidoService();
        System.out.println("Calculando descontos e processando pagamento...");
        return pedidoService.processarPedido(pedido);
    }

    private static PedidoService configurarPedidoService() {
        Consumer<String> consolePrinter = System.out::println;

        GatewayExterno gatewayExternoMock = new GatewayExterno();
        GatewayPagamento gatewayAdapter = new GatewayPagamentoAdapter(gatewayExternoMock); 
        ServicoSugestaoIA geminiAI = new AdaptadorSugestaoGemini();
        AdaptadorNotificacaoPush pushAdapter = new AdaptadorNotificacaoPush(consolePrinter);
        PedidoService pedidoService = new PedidoService(gatewayAdapter, geminiAI);
        pedidoService.registrarObservador(pushAdapter);
        
        return pedidoService;
    }
}
