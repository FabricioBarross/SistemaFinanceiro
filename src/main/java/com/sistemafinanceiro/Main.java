package com.sistemafinanceiro;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(new Locale("pt", "BR"));

        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine().trim();
        while (!nome.matches("^[a-zA-Z\\s]+$")) {
            System.out.print("Digite um nome sem números: ");
            nome = scanner.nextLine().trim();
        }

        double rendaMensal = lerDouble(scanner, "Digite sua renda mensal: ");

        double despesasFixas = lerDouble(scanner, "Digite suas despesas fixas mensais: ");

        double despesasVariaveis = lerDouble(scanner, "Digite suas despesas variáveis mensais: ");

        System.out.println("\n");

        double despesasTotais = despesasFixas + despesasVariaveis;
        double saldoFinal = rendaMensal - despesasTotais;

        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        System.out.println("======DADOS FINANCEIROS======");
        System.out.println("Olá, " + nome + "!");
        System.out.println("Sua renda mensal é: " + moeda.format(rendaMensal));
        System.out.println("Suas despesas fixas mensais são: " + moeda.format(despesasFixas));
        System.out.println("Suas despesas variáveis mensais são: " + moeda.format(despesasVariaveis));
        System.out.println("Seu saldo final é: " + moeda.format(saldoFinal));
        scanner.close();

        System.out.println("\n");

        if (saldoFinal == 0) {
            System.out.println("Cuidado! Você está gastando exatamente o que ganha.");
        } else if (saldoFinal < 0) {
            System.out.println("Atenção! Você está gastando mais do que ganha.");
        } else {
            System.out.println("Parabéns! Suas despesas não ultrapassaram sua renda.");
        }

    }

    private static double lerDouble(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);

            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            }

            System.out.println("Valor inválido, digite apenas números nesse campo.");
            scanner.next();
        }
    }


}
