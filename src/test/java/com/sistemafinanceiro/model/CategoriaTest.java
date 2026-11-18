package com.sistemafinanceiro.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoriaTest {

    @Test
    void categoriasDeDespesaNaoIncluemSalario() {
        assertFalse(Categoria.para(TipoMovimentacao.DESPESA).contains(Categoria.SALARIO));
        assertTrue(Categoria.para(TipoMovimentacao.DESPESA).contains(Categoria.OUTROS));
    }

    @Test
    void categoriasDeReceitaNaoIncluemMoradia() {
        assertFalse(Categoria.para(TipoMovimentacao.RECEITA).contains(Categoria.MORADIA));
        assertTrue(Categoria.para(TipoMovimentacao.RECEITA).contains(Categoria.OUTROS));
    }
}