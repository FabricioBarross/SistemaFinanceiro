package com.sistemafinanceiro;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(new Locale("pt", "BR"));

        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine().trim();
        while (!nome.matches("^[\\p{L}\\s]+$")) {
            System.out.print("Digite um nome sem números: ");
            nome = scanner.nextLine().trim();
        }

        BigDecimal rendaMensal = lerBigDecimal(scanner, "Digite sua renda mensal: ");
        BigDecimal despesasFixas = lerBigDecimal(scanner, "Digite suas despesas fixas mensais: ");
        BigDecimal despesasVariaveis = lerBigDecimal(scanner, "Digite suas despesas variáveis mensais: ");
        BigDecimal despesasTotais = calcularDespesasTotais(despesasFixas, despesasVariaveis);
        BigDecimal saldoFinal = calcularSaldoFinal(rendaMensal, despesasTotais);
        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        System.out.println("\n");

        System.out.println("======DADOS FINANCEIROS======");
        System.out.println("Olá, " + nome + "!");
        System.out.println("Sua renda mensal é: " + moeda.format(rendaMensal));
        System.out.println("Suas despesas fixas mensais são: " + moeda.format(despesasFixas));
        System.out.println("Suas despesas variáveis mensais são: " + moeda.format(despesasVariaveis));
        System.out.println("Seu saldo final é: " + moeda.format(saldoFinal));

        System.out.println("\n");

        if (saldoFinal.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Cuidado! Você está gastando exatamente o que ganha.");
        } else if (saldoFinal.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Atenção! Você está gastando mais do que ganha.");
        } else {
            System.out.println("Parabéns! Suas despesas não ultrapassaram sua renda.");
        }
        scanner.close();
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
            BigDecimal despesasTotais
    ) {
        return rendaMensal.subtract(despesasTotais);
    }
}
