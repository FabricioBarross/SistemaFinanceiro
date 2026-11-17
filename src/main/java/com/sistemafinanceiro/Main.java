package com.sistemafinanceiro;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.sistemafinanceiro.model.TipoMovimentacao;
import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.service.ServicoFinanceiro;

public class Main {
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final String NOME_PROJETO = "Sistema Financeiro";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        exibirNomeProjeto();

        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine().trim();

        while (nome.isBlank() || !nome.matches("^[\\p{L}\\s]+$")) {
            System.out.print("Digite um nome válido: ");
            nome = scanner.nextLine().trim();
        }

        ServicoFinanceiro servico = new ServicoFinanceiro();

       int opcao = 0;

       while (opcao != 6) {

           exibirMenu();

           opcao = lerOpcao(scanner);

           switch (opcao) {

               case 1 -> {
                   BigDecimal valor = lerBigDecimal(scanner, "Digite sua renda mensal: ");
                   servico.adicionar(new Movimentacao("Renda mensal", valor, TipoMovimentacao.RECEITA, "Trabalho"));
                   System.out.println("Renda mensal cadastrada com sucesso!");
               }
               case 2 -> {
                   BigDecimal valor = lerBigDecimal(scanner, "Digite sua renda variável: ");
                   servico.adicionar(new Movimentacao("Renda extra", valor, TipoMovimentacao.RECEITA, "renda extra"));
                   System.out.println("Renda extra cadastrada com sucesso!");
               }
               case 3 -> {
                   BigDecimal valor = lerBigDecimal(scanner, "Digite suas despesa fixa: ");
                   servico.adicionar(new Movimentacao("Despesas fixa", valor, TipoMovimentacao.DESPESA, "contas"));
                   System.out.println("Despesas fixa cadastrada com sucesso!");
               }
               case 4 -> {
                   BigDecimal valor = lerBigDecimal(scanner, "Digite suas despesas variáveis: ");
                   servico.adicionar(new Movimentacao("Despesas variáveis", valor, TipoMovimentacao.DESPESA, "Imprevistos"));
                   System.out.println("Despesas variáveis cadastrada com sucesso!");
               }
               case 5 -> {
                   consultarDadosFinanceiros(nome, servico);
               }
               case 6 -> {
                   System.out.println("Saindo do sistema...");
               }
               default ->
                   System.out.println("Opção inválida. Tente novamente.");
           }
       }

        scanner.close();

    }
    private static void exibirNomeProjeto() {
        System.out.println("Bem-vindo ao " + NOME_PROJETO + "!");
    }

    private static BigDecimal lerBigDecimal(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                BigDecimal valor = new BigDecimal(entrada.replace(",", "."));

                if (valor.signum() >= 0) {
                    return valor;
                }
                System.out.println("O valor não pode ser negativo.");

            } catch (NumberFormatException e) {
                System.out.println("Digite um valor monetário válido.");
            }
        }
    }

    private static int lerOpcao(Scanner scanner) {
        while (true) {
            System.out.print("Escolha uma opção: ");

            String entrada = scanner.nextLine().trim();

            try {
                int opcao = Integer.parseInt(entrada);

                if (opcao >= 1 && opcao <= 6) {
                    return opcao;
                }

                System.out.println(
                        "Opção inválida. Digite um número entre 1 e 6."
                );

            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números inteiros.");
            }
        }
    }
    private static void exibirMenu() {
        System.out.println("\n======MENU======");
        System.out.println("1. Cadastrar renda mensal");
        System.out.println("2. Cadastrar renda variável");
        System.out.println("3. Cadastrar despesas fixas");
        System.out.println("4. Cadastrar despesas variáveis");
        System.out.println("5. Consultar dados financeiros");
        System.out.println("6. Sair");
    }
    private static void consultarDadosFinanceiros(String nome, ServicoFinanceiro servico) {
        if (servico.estaVazio()) {
            System.out.println("Nenhuma movimentção cadastrada.");
            return;
        }
        NumberFormat moeda = NumberFormat.getCurrencyInstance(LOCALE_BR);
        BigDecimal saldo = servico.saldo();

        System.out.println("\n====== DADOS FINANCEIROS ======");
        System.out.println("Olá, " + nome + "!");
        System.out.println("Sua renda total é: " + moeda.format(servico.totalReceitas()));
        System.out.println("Suas despesas totais são: " + moeda.format(servico.totalDespesas()));
        System.out.println("Seu saldo final é: " + moeda.format(saldo));
        System.out.println();

        System.out.println("\n====== MOVIMENTAÇÕES ======");
        for (Movimentacao m : servico.listar()) {
            System.out.println("-------------------------");
            System.out.println("Descrição: " + m.getDescricao());
            System.out.println("Valor: " + moeda.format(m.getValor()));
            System.out.println("Tipo: " + m.getTipo());
            System.out.println("Categoria: " + m.getCategoria());
        }
        System.out.println();
        int sinal = saldo.signum();

        if (sinal == 0) {
            System.out.println("Cuidado! Você está gastando exatamente o que ganha.");
        } else if (sinal < 0) {
            System.out.println("Atenção! Você está gastando mais do que ganha.");
        } else {
            System.out.println("Parabéns! Suas despesas não ultrapssaram sua renda.");
        }
    }
}
