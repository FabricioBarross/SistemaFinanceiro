package com.sistemafinanceiro.service;

import com.sistemafinanceiro.model.Categoria;
import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.model.TipoMovimentacao;
import com.sistemafinanceiro.repository.RepositorioEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ServicoFinanceiroTest {

    private ServicoFinanceiro servico;

    @BeforeEach
    void setUp() {
        servico = new ServicoFinanceiro(new RepositorioEmMemoria());
    }

    private Movimentacao receita(String valor) {
        return new Movimentacao("Receita", new BigDecimal(valor),
                TipoMovimentacao.RECEITA, Categoria.SALARIO, LocalDate.of(2026, 1, 15));
    }

    private Movimentacao despesa(String valor) {
        return new Movimentacao("Despesa", new BigDecimal(valor),
                TipoMovimentacao.DESPESA, Categoria.ALIMENTACAO, LocalDate.of(2026, 1, 15));
    }

    @Test
    void servicoNovoEstaVazioComSaldoZero() {
        assertTrue(servico.estaVazio());
        assertEquals(0, servico.saldo().compareTo(BigDecimal.ZERO));
    }

    @Test
    void somaDecimaisSemErroDePontoFlutuante() {
        servico.adicionar(receita("0.1"));
        servico.adicionar(receita("0.1"));
        servico.adicionar(receita("0.1"));

        assertEquals(0, servico.totalReceitas().compareTo(new BigDecimal("0.3")));
    }

    @Test
    void saldoEReceitasMenosDespesas() {
        servico.adicionar(receita("3000.00"));
        servico.adicionar(receita("500.50"));
        servico.adicionar(despesa("1200.25"));

        assertEquals(0, servico.saldo().compareTo(new BigDecimal("2300.25")));
    }

    @Test
    void saldoNegativoQuandoDespesaMaiorQueReceita() {
        servico.adicionar(receita("100"));
        servico.adicionar(despesa("250"));

        assertEquals(-1, servico.saldo().signum());
    }

    @Test
    void listarRetornaListaImutavel() {
        servico.adicionar(receita("100"));

        assertThrows(UnsupportedOperationException.class,
                () -> servico.listar().clear());
    }

    @Test
    void adicionarNuloLancaExcecao() {
        assertThrows(NullPointerException.class, () -> servico.adicionar(null));
    }

    @Test
    void totalDespesasSomaSoDespesas() {
        servico.adicionar(receita("1000"));
        servico.adicionar(despesa("200.10"));
        servico.adicionar(despesa("50.20"));
        assertEquals(0, servico.totalDespesas().compareTo(new BigDecimal("250.30")));
    }

    @Test
    void listarPorPeriodoEhInclusivo() {
        servico.adicionar(new Movimentacao("a", BigDecimal.TEN, TipoMovimentacao.DESPESA,
                Categoria.LAZER, LocalDate.of(2026, 1, 1)));
        servico.adicionar(new Movimentacao("b", BigDecimal.TEN, TipoMovimentacao.DESPESA,
                Categoria.LAZER, LocalDate.of(2026, 1, 31)));
        servico.adicionar(new Movimentacao("c", BigDecimal.TEN, TipoMovimentacao.DESPESA,
                Categoria.LAZER, LocalDate.of(2026, 2, 1)));

        assertEquals(2, servico.listarPorPeriodo(
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)).size());
    }

    @Test
    void totalPorCategoriaAgrupaCorretamente() {
        servico.adicionar(despesa("10.00")); // ALIMENTACAO
        servico.adicionar(despesa("5.50"));  // ALIMENTACAO
        var totais = servico.totalPorCategoria(TipoMovimentacao.DESPESA);
        assertEquals(0, totais.get(Categoria.ALIMENTACAO).compareTo(new BigDecimal("15.50")));
    }

    @Test
    void removerApagaPorId() {
        Movimentacao m = receita("100");
        servico.adicionar(m);

        assertTrue(servico.remover(m.getId()));
        assertTrue(servico.estaVazio());
    }

    @Test
    void removerIdInexistenteRetornaFalse() {
        assertFalse(servico.remover(java.util.UUID.randomUUID()));
    }

    @Test
    void listarPorPeriodoComFimAntesDoInicioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                servico.listarPorPeriodo(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1)));
    }
}