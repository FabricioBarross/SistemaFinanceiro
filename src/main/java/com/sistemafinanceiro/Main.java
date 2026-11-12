package com.sistemafinanceiro;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;
import java.math.BigDecimal;

public class Main {
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final String NOME_PROJETO = "Sistema Financeiro";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(LOCALE_BR);
        NumberFormat moeda = NumberFormat.getCurrencyInstance(LOCALE_BR);

        exibirNomeProjeto();

        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine().trim();

        while (!nome.matches("^[\\p{L}\\s]+$")) {
            System.out.print("Digite um nome sem números: ");
            nome = scanner.nextLine().trim();
        }

       int opcao = 0;

       BigDecimal rendaMensal = BigDecimal.ZERO;
       BigDecimal rendaVariavel = BigDecimal.ZERO;
       BigDecimal despesasFixas = BigDecimal.ZERO;
       BigDecimal despesasVariaveis = BigDecimal.ZERO;

       while (opcao != 10) {
           System.out.println("=====MENU DE OPÇÕES=====");
           System.out.println("1. Cadastrar renda mensal");
           System.out.println("2. Cadastrar renda variável");
           System.out.println("3. Cadastrar despesas fixas");
           System.out.println("4. Cadastrar despesas variáveis");
           System.out.println("5. Editar renda mensal");
           System.out.println("6. Editar renda variável");
           System.out.println("7. Editar despesas fixas");
           System.out.println("8. Editar despesas variáveis");
           System.out.println("9. Consultar saldo");
           System.out.println("10. Sair");
           System.out.print("Escolha uma opção: ");

           opcao = scanner.nextInt();

           switch (opcao) {
               case 1:
                   rendaMensal = lerBigDecimal(scanner, "Digite o valor da renda mensal: ");
                   System.out.println("Renda mensal cadastrada com sucesso!");
                   break;

               case 2:
                   rendaVariavel = lerBigDecimal(scanner, "Digite o valor da renda variável: ");
                   System.out.println("Renda variável cadastrada com sucesso!");
                   break;

               case 3:
                   despesasFixas = lerBigDecimal(scanner, "Digite o valor das despesas fixas: ");
                   System.out.println("Despesas fixas cadastradas com sucesso!");
                   break;

               case 4:
                   despesasVariaveis = lerBigDecimal(scanner, "Digite o valor das despesas variáveis: ");
                   System.out.println("Despesas variáveis cadastradas com sucesso!");
                   break;

               case 5:
                   rendaMensal = lerBigDecimal(scanner, "Digite o novo valor da renda mensal: ");
                   System.out.println("Renda mensal editada com sucesso!");
                   break;

               case 6:
                   rendaVariavel = lerBigDecimal(scanner, "Digite o novo valor da renda variável: ");
                   System.out.println("Renda variável editada com sucesso!");
                   break;
               case 7:
                   despesasFixas = lerBigDecimal(scanner, "Digite o novo valor das despesas fixas: ");
                   System.out.println("Despesas fixas editadas com sucesso!");
                   break;

               case 8:
                   despesasVariaveis = lerBigDecimal(scanner, "Digite o novo valor das despesas variáveis: ");
                   System.out.println("Despesas variáveis editadas com sucesso!");
                   break;

               case 9:
                     BigDecimal despesasTotais = calcularDespesasTotais(despesasFixas, despesasVariaveis);
                     BigDecimal saldoFinal = calcularSaldoFinal(rendaMensal, rendaVariavel, despesasTotais);

                   System.out.println("======DADOS FINANCEIROS======");
                   System.out.println("Olá, " + nome + "!");
                   System.out.println("Sua renda mensal é: " + moeda.format(rendaMensal));
                   System.out.println("Sua renda variável é: " + moeda.format(rendaVariavel));
                   System.out.println("Suas despesas fixas mensais são: " + moeda.format(despesasFixas));
                   System.out.println("Suas despesas variáveis mensais são: " + moeda.format(despesasVariaveis));
                   System.out.println("Suas despesas totais são: " + moeda.format(despesasTotais));
                   System.out.println("Seu saldo final é: " + moeda.format(saldoFinal));

                   System.out.println("\n");

                   if (saldoFinal.compareTo(BigDecimal.ZERO) == 0) {
                       System.out.println("Cuidado! Você está gastando exatamente o que ganha.");
                   } else if (saldoFinal.compareTo(BigDecimal.ZERO) < 0) {
                       System.out.println("Atenção! Você está gastando mais do que ganha.");
                   } else {
                       System.out.println("Parabéns! Suas despesas não ultrapassaram sua renda.");
                   }
                   break;

               case 10:
                   System.out.println("Saindo do sistema...");
                   break;

               default:
                   System.out.println("Opção inválida. Tente novamente.");
           }
       }

        System.out.println("\n");

        scanner.close();

    }
    private static void exibirNomeProjeto() {
        System.out.println("Bem-vindo ao " + NOME_PROJETO + "!");
    }
    private static BigDecimal lerBigDecimal(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            if (scanner.hasNextBigDecimal()) {
                BigDecimal valor = scanner.nextBigDecimal();
                if (valor.signum() >= 0) {
                    return valor;
                } else {
                    System.out.println("O valor não pode ser negativo.");
                }
            } else {
                System.out.println("Digite apenas números nesse campo.");
                scanner.next();
            }
        }

    }
    private static BigDecimal calcularDespesasTotais(
            BigDecimal despesasFixas,
            BigDecimal despesasVariaveis
    ) {
        return despesasFixas.add(despesasVariaveis);
    }
    private static BigDecimal calcularSaldoFinal(
            BigDecimal rendaMensal,
            BigDecimal rendaVariavel,
            BigDecimal despesasTotais
    ) {
        return rendaMensal.add(rendaVariavel).subtract(despesasTotais);
    }

}
