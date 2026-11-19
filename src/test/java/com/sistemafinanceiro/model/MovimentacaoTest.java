package com.sistemafinanceiro.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovimentacaoTest {

    private static final LocalDate DATA = LocalDate.of(2026, 1, 15);

    @Test
    void rejeitaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", new BigDecimal("-1"), TipoMovimentacao.RECEITA, Categoria.SALARIO, DATA));
    }

    @Test
    void rejeitaValorNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", null, TipoMovimentacao.RECEITA, Categoria.SALARIO, DATA));
    }

    @Test
    void rejeitaDescricaoEmBranco() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("  ", BigDecimal.ONE, TipoMovimentacao.RECEITA, Categoria.SALARIO, DATA));
    }

    @Test
    void rejeitaTipoNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", BigDecimal.ONE, null, Categoria.SALARIO, DATA));
    }

    @Test
    void rejeitaCategoriaNula() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", BigDecimal.ONE, TipoMovimentacao.RECEITA, null, DATA));
    }

    @Test
    void rejeitaDataNula() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", BigDecimal.ONE, TipoMovimentacao.RECEITA, Categoria.SALARIO, null));
    }

    @Test
    void rejeitaCategoriaIncompativelComTipo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", BigDecimal.ONE, TipoMovimentacao.DESPESA, Categoria.SALARIO, DATA));
    }

    @Test
    void categoriaOutrosServeParaReceitaEDespesa() {
        assertDoesNotThrow(() ->
                new Movimentacao("x", BigDecimal.ONE, TipoMovimentacao.RECEITA, Categoria.OUTROS, DATA));
        assertDoesNotThrow(() ->
                new Movimentacao("x", BigDecimal.ONE, TipoMovimentacao.DESPESA, Categoria.OUTROS, DATA));
    }

    @Test
    void rejeitaValorZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", BigDecimal.ZERO, TipoMovimentacao.RECEITA, Categoria.SALARIO, DATA));
    }

    @Test
    void rejeitaMaisDeDuasCasasDecimais() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", new BigDecimal("10.999"), TipoMovimentacao.RECEITA, Categoria.SALARIO, DATA));
    }

    @Test
    void aceitaZerosAMaisENormalizaParaDuasCasas() {
        var m = new Movimentacao("x", new BigDecimal("10.500"), TipoMovimentacao.RECEITA, Categoria.SALARIO, DATA);
        assertEquals("10.50", m.getValor().toPlainString());
    }

    @Test
    void descricaoEhAparada() {
        var m = new Movimentacao("  mercado  ", BigDecimal.ONE, TipoMovimentacao.DESPESA, Categoria.ALIMENTACAO, DATA);
        assertEquals("mercado", m.getDescricao());
    }
}