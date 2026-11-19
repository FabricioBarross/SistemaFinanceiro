package com.sistemafinanceiro;

import com.sistemafinanceiro.model.Categoria;
import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.model.TipoMovimentacao;
import com.sistemafinanceiro.repository.RepositorioException;
import com.sistemafinanceiro.repository.RepositorioSqlite;
import com.sistemafinanceiro.service.ServicoFinanceiro;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final Locale LOCALE_BR = Locale.of("pt", "BR");
    private static final String NOME_PROJETO = "Sistema Financeiro";
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final int OPCAO_SAIR = 7;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            executar(scanner);
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada. Saindo do sistema...");
        } catch (RepositorioException e) {
            System.out.println("Não foi possivel acessar o banco de dados: " + e.getMessage());
        }
    }

    private static void executar(Scanner scanner) {
        exibirNomeProjeto();

        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine().trim();

        while (nome.isBlank() || !nome.matches("^[\\p{L}\\s]+$")) {
            System.out.print("Digite um nome válido: ");
            nome = scanner.nextLine().trim();
        }

        ServicoFinanceiro servico = new ServicoFinanceiro(new RepositorioSqlite("financeiro.db"));

        int opcao = 0;

        while (opcao != OPCAO_SAIR) {
            exibirMenu();
            opcao = lerOpcao(scanner, 1, OPCAO_SAIR);

            try {
                switch (opcao) {
                    case 1 -> registrar(scanner, servico, TipoMovimentacao.RECEITA);
                    case 2 -> registrar(scanner, servico, TipoMovimentacao.DESPESA);
                    case 3 -> consultarDadosFinanceiros(nome, servico);
                    case 4 -> consultarPorPeriodo(scanner, servico);
                    case 5 -> consultarTotaisPorCategoria(servico);
                    case 6 -> removerMovimentacao(scanner, servico);
                    case OPCAO_SAIR -> System.out.println("Saindo do sistema...");
                }
            } catch (RepositorioException e) {
                System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
            }
        }
    }

    private static void exibirNomeProjeto() {
        System.out.println("Bem-vindo ao " + NOME_PROJETO + "!");
    }

    private static void exibirMenu() {
        System.out.println("\n======MENU======");
        System.out.println("1. Registrar receita");
        System.out.println("2. Registrar despesa");
        System.out.println("3. Consultar dados financeiros");
        System.out.println("4. Consultar por período");
        System.out.println("5. Totais por categoria");
        System.out.println("6. Remover movimetação");
        System.out.println("7. Sair");
    }

                               //-----------ações do menu------------//

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
            imprimirMovimentacao(m, moeda);
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

    private static void consultarPorPeriodo(Scanner scanner, ServicoFinanceiro servico) {
        System.out.println("\n--- Consulta por período ---");
        LocalDate inicio = lerData(scanner, "Data inicial (dd/mm/aaaa): ");
        LocalDate fim = lerData(scanner, "Data final (dd/mm/aaaa): ");

        List<Movimentacao> encontradas;
        try {
            encontradas = servico.listarPorPeriodo(inicio, fim);
        } catch (IllegalArgumentException e) {
            System.out.println("A data final não pode ser anterior à data inicial.");
            return;
        }

        if (encontradas.isEmpty()) {
            System.out.println("Nenhuma movimentação no período.");
            return;
        }

        NumberFormat moeda = NumberFormat.getCurrencyInstance(LOCALE_BR);
        System.out.println("\n====== MOVIMENTAÇÕES DE " + inicio.format(FORMATO_DATA)
                + " A " + fim.format(FORMATO_DATA) + " ======");
        for (Movimentacao m : encontradas) {
            imprimirMovimentacao(m, moeda);
        }
        System.out.println("-------------------------");
        System.out.println("Total de movimentações: " + encontradas.size());
    }

    private static void consultarTotaisPorCategoria(ServicoFinanceiro servico) {
        if (servico.estaVazio()) {
            System.out.println("Nenhuma movimentação cadastrada.");
            return;
        }

        NumberFormat moeda = NumberFormat.getCurrencyInstance(LOCALE_BR);

        for (TipoMovimentacao tipo : TipoMovimentacao.values()) {
            String titulo = tipo == TipoMovimentacao.RECEITA ? "RECEITAS" : "DESPESAS";
            System.out.println("\n====== " + titulo + " POR CATEGORIA ======");

            Map<Categoria, BigDecimal> totais = servico.totalPorCategoria(tipo);
            if (totais.isEmpty()) {
                System.out.println("Nenhuma movimentação.");
                continue;
            }
            totais.forEach((categoria, total) ->
                    System.out.println(categoria.getNomeExibicao() + ": " + moeda.format(total)));
        }
    }

    private static void removerMovimentacao(Scanner scanner, ServicoFinanceiro servico) {
        List<Movimentacao> lista = servico.listar();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma movimentação para remover.");
            return;
        }

        NumberFormat moeda = NumberFormat.getCurrencyInstance(LOCALE_BR);

        System.out.println("\n====== REMOVER MOVIMENTAÇÃO ======");
        for (int i = 0; i < lista.size(); i++) {
            Movimentacao m = lista.get(i);
            System.out.println((i + 1) + ". " + m.getData().format(FORMATO_DATA)
                    + " | " + m.getTipo()
                    + " | " + m.getDescricao()
                    + " | " + moeda.format(m.getValor()));
        }
        System.out.println("0. Cancelar");

        int escolha = lerOpcao(scanner, 0, lista.size());
        if (escolha == 0) {
            System.out.println("Remoção cancelada.");
            return;
        }

        Movimentacao alvo = lista.get(escolha - 1);
        if (!confirmar(scanner, "Remover \"" + alvo.getDescricao() + "\"? Não dá para desfazer.")) {
            System.out.println("Remoção cancelada.");
            return;
        }

        if (servico.remover(alvo.getId())) {
            System.out.println("Movimentação removida com sucesso!");
        } else {
            System.out.println("A movimentação não foi encontrada.");
        }
    }
                           // ---------- Exibição ----------

    private static void imprimirMovimentacao(Movimentacao m, NumberFormat moeda) {
        System.out.println("-------------------------");
        System.out.println("Descrição: " + m.getDescricao());
        System.out.println("Valor: " + moeda.format(m.getValor()));
        System.out.println("Data: " + m.getData().format(FORMATO_DATA));
        System.out.println("Tipo: " + m.getTipo());
        System.out.println("Categoria: " + m.getCategoria().getNomeExibicao());
    }

                         // ---------- Leitura de entrada ----------

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
                return Movimentacao.normalizarValor(new BigDecimal(entrada.replace(",", ".")));
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor monetário válido.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
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

    private static boolean confirmar(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem + " (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase(LOCALE_BR);
            if (resposta.equals("s")) return true;
            if (resposta.equals("n")) return false;
            System.out.println("Responda com s ou n.");
        }
    }
}