package com.sistemafinanceiro;

import com.sistemafinanceiro.model.TipoMovimentacao;
import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.service.ServicoFinanceiro;
import com.sistemafinanceiro.model.Categoria;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.text.NumberFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Main {
    private static final Locale LOCALE_BR = Locale.of("pt", "BR");
    private static final String NOME_PROJETO = "Sistema Financeiro";
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);


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

        while (opcao != 4) {
            exibirMenu();
            opcao = lerOpcao(scanner, 1, 4);

            switch (opcao) {
                case 1 -> registrar(scanner, servico, TipoMovimentacao.RECEITA);
                case 2 -> registrar(scanner, servico, TipoMovimentacao.DESPESA);
                case 3 -> consultarDadosFinanceiros(nome, servico);
                case 4 -> System.out.println("Saindo do sistema...");
            }
        }
        scanner.close();
    }

    private static void exibirNomeProjeto() {
        System.out.println("Bem-vindo ao " + NOME_PROJETO + "!");
    }

    private static void exibirMenu() {
        System.out.println("\n======MENU======");
        System.out.println("1. Registrar receita");
        System.out.println("2. Registrar despesa");
        System.out.println("3. Consultar dados financeiros");
        System.out.println("4. Sair");
    }

    private static void registrar(Scanner scanner, ServicoFinanceiro servico, TipoMovimentacao tipo) {
        String nomeTipo = tipo == TipoMovimentacao.RECEITA ? "Receita" : "Despesa";

        System.out.println("\n--- Nova " + nomeTipo.toLowerCase() + " ---");

        String descricao = lerTexto(scanner, "Descrição: ");
        BigDecimal valor = lerBigDecimal(scanner, "Valor: ");
        Categoria categoria = lerCategoria(scanner, tipo);
        LocalDate data = lerData(scanner, "Data (dd/mm/aaaa): ");

        servico.adicionar(new Movimentacao(descricao, valor, tipo, categoria, data));
        System.out.println(nomeTipo + " cadastrada com sucesso!");
    }

    private static String lerTexto(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isBlank()) {
                return entrada;
            }
            System.out.println("O campo não pode ficar vazio.");
        }
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

    private static int lerOpcao(Scanner scanner, int minimo, int maximo) {
        while (true) {
            System.out.print("Escolha uma opção: ");
            String entrada = scanner.nextLine().trim();

            try {
                int opcao = Integer.parseInt(entrada);

                if (opcao >= minimo && opcao <= maximo) {
                    return opcao;
                }
                System.out.println("Opção inválida. Digite um número entre " + minimo + " e " + maximo + ".");

            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números inteiros.");
            }
        }
    }

    private static Categoria lerCategoria(Scanner scanner, TipoMovimentacao tipo) {
        List<Categoria> opcoes = Categoria.para(tipo);

        System.out.println("Categorias:");
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i + 1) + ". " + opcoes.get(i).getNomeExibicao());
        }

        int escolha = lerOpcao(scanner, 1, opcoes.size());
        return opcoes.get(escolha - 1);
    }

    private static LocalDate lerData(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return LocalDate.parse(entrada, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/mm/aaaa.");
            }
        }
    }

    private static void consultarDadosFinanceiros(String nome, ServicoFinanceiro servico) {
        if (servico.estaVazio()) {
            System.out.println("Nenhuma movimentação cadastrada.");
            return;
        }

        NumberFormat moeda = NumberFormat.getCurrencyInstance(LOCALE_BR);
        BigDecimal saldo = servico.saldo();

        System.out.println("\n====== DADOS FINANCEIROS ======");
        System.out.println("Olá, " + nome + "!");
        System.out.println("Sua renda total é: " + moeda.format(servico.totalReceitas()));
        System.out.println("Suas despesas totais são: " + moeda.format(servico.totalDespesas()));
        System.out.println("Seu saldo final é: " + moeda.format(saldo));

        System.out.println("\n====== MOVIMENTAÇÕES ======");
        for (Movimentacao m : servico.listar()) {
            System.out.println("-------------------------");
            System.out.println("Descrição: " + m.getDescricao());
            System.out.println("Valor: " + moeda.format(m.getValor()));
            System.out.println("Data: " + m.getData().format(FORMATO_DATA));
            System.out.println("Tipo: " + m.getTipo());
            System.out.println("Categoria: " + m.getCategoria().getNomeExibicao());
        }

        System.out.println();
        int sinal = saldo.signum();

        if (sinal == 0) {
            System.out.println("Cuidado! Você está gastando exatamente o que ganha.");
        } else if (sinal < 0) {
            System.out.println("Atenção! Você está gastando mais do que ganha.");
        } else {
            System.out.println("Parabéns! Suas despesas não ultrapassaram sua renda.");
        }
    }
}