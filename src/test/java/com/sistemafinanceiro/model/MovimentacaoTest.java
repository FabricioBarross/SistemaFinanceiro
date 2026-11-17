package com.sistemafinanceiro.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MovimentacaoTest {

    @Test
    void rejeitaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", new BigDecimal("-1"), TipoMovimentacao.RECEITA, "y"));
    }

    @Test
    void rejeitaValorNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", null, TipoMovimentacao.RECEITA, "y"));
    }

    @Test
    void rejeitaDescricaoEmBranco() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("  ", BigDecimal.ONE, TipoMovimentacao.RECEITA, "y"));
    }

    @Test
    void rejeitaTipoNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", BigDecimal.ONE, null, "y"));
    }

    @Test
    void rejeitaCategoriaEmBranco() {
        assertThrows(IllegalArgumentException.class, () ->
                new Movimentacao("x", BigDecimal.ONE, TipoMovimentacao.RECEITA, ""));
    }
}