package com.sistemafinanceiro.repository;

import com.sistemafinanceiro.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioSqliteTest {

    @TempDir
    Path pasta;

    private Movimentacao exemplo() {
        return new Movimentacao("Salário", new BigDecimal("1234.56"),
                TipoMovimentacao.RECEITA, Categoria.SALARIO, LocalDate.of(2026, 1, 15));
    }

    @Test
    void dadosSobrevivemEntreInstancias() {
        String arquivo = pasta.resolve("teste.db").toString();
        Movimentacao original = exemplo();

        new RepositorioSqlite(arquivo).salvar(original);
        var lidas = new RepositorioSqlite(arquivo).listar(); // "reabriu o programa"

        assertEquals(1, lidas.size());
        assertEquals(original, lidas.get(0));
        assertEquals("1234.56", lidas.get(0).getValor().toPlainString());
    }

    @Test
    void removerApagaDoBanco() {
        var repo = new RepositorioSqlite(pasta.resolve("teste.db").toString());
        Movimentacao m = exemplo();
        repo.salvar(m);

        assertTrue(repo.remover(m.getId()));
        assertTrue(repo.listar().isEmpty());
        assertFalse(repo.remover(m.getId()));
    }
}