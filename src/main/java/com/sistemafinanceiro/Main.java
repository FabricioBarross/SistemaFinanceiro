package com.sistemafinanceiro;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.sistemafinanceiro.model.TipoMovimentacao;
import com.sistemafinanceiro.model.Movimentacao;

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

        ArrayList<Movimentacao> movimentacoes = new ArrayList<>();

       int opcao = 0;

       while (opcao != 6) {

           exibirMenu();

           opcao = lerOpcao(scanner);

           switch (opcao) {
               case 1:
                  BigDecimal rendaMensal = lerBigDecimal(scanner, "Digite sua renda mensal: ");

                  movimentacoes.add(new Movimentacao(
                          "Renda mensal",
                          rendaMensal,
                          TipoMovimentacao.RECEITA,
                          "Trabalho"
                  ));
                  System.out.println("Renda mensal cadastrada com sucesso!");
                   break;

               case 2:
                   BigDecimal rendaVariavel = lerBigDecimal(scanner, "Digite o valor da renda variável: ");

                   movimentacoes.add(new Movimentacao(
                           "Renda Variável",
                           rendaVariavel,
                           TipoMovimentacao.RECEITA,
                           "Renda extra"
                   ));

                   System.out.println("Renda variável cadastrada com sucesso!");
                   break;

               case 3:
                    BigDecimal despesasFixas = lerBigDecimal(scanner, "Digite o valor das despesas fixas: ");

                    movimentacoes.add(new Movimentacao(
                            "Despesas fixas",
                            despesasFixas,
                            TipoMovimentacao.DESPESA,
                            "Despesas fixas"
                    ));

                   System.out.println("Despesas fixas cadastradas com sucesso!");
                   break;

               case 4:
                   BigDecimal despesasVariaveis = lerBigDecimal(scanner, "Digite o valor das despesas variáveis: ");

                   movimentacoes.add(new Movimentacao(
                           "Despesas variáveis",
                           despesasVariaveis,
                           TipoMovimentacao.DESPESA,
                           "Despesas variáveis"
                   ));

                   System.out.println("Despesas variáveis cadastradas com sucesso!");
                   break;

               case 5:
                     consultarDadosFinanceiros(nome, movimentacoes);
                   break;

               case 6:
                   System.out.println("Saindo do sistema...");
                   break;

               default:
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
    private static void consultarDadosFinanceiros(
            String nome,
           ArrayList<Movimentacao> movimentacoes

    ) {
        if (movimentacoes.isEmpty()){
            System.out.println("Nenhuma movimentção cadastrada.");
            return;
        }
        NumberFormat moeda = NumberFormat.getCurrencyInstance(LOCALE_BR);
        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;

        for(Movimentacao movimentacao : movimentacoes) {
            if (movimentacao.getTipo() == TipoMovimentacao.RECEITA) {
                totalReceitas = totalReceitas.add(movimentacao.getValor());
            } else if (movimentacao.getTipo() == TipoMovimentacao.DESPESA) {
                totalDespesas = totalDespesas.add(movimentacao.getValor());
            }
        }
        BigDecimal saldoFinal = totalReceitas.subtract(totalDespesas);

        System.out.println("\n====== DADOS FINANCEIROS ======");

        System.out.println("Olá, " + nome + "!");

        System.out.println("Sua renda total é: " + moeda.format(totalReceitas));

        System.out.println("Suas despesas totais são: " + moeda.format(totalDespesas));

        System.out.println("Seu saldo final é: " + moeda.format(saldoFinal));

        System.out.println();

        System.out.println("\n====== MOVIMENTAÇÕES ======");
        for (Movimentacao movimentacao : movimentacoes) {
            System.out.println("-------------------------");

            System.out.println(
                    "Descrição: " + movimentacao.getDescricao()
            );

            System.out.println(
                    "Valor: " + moeda.format(movimentacao.getValor())
            );

            System.out.println(
                    "Tipo: " + movimentacao.getTipo()
            );

            System.out.println(
                    "Categoria: " + movimentacao.getCategoria()
            );
        }

        System.out.println();

        if (saldoFinal.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Cuidado! Você está gastando exatamente o que ganha.");
        } else if (saldoFinal.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Atenção! Você está gastando mais do que ganha.");
        } else {
            System.out.println("Parabéns! Suas despesas não ultrapassaram sua renda.");
        }
    }
}
