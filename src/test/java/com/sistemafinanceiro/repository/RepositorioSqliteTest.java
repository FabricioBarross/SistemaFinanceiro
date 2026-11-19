package com.sistemafinanceiro.repository;

import com.sistemafinanceiro.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

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

    @Test
    void salvarMesmoIdAtualizaEmVezDeDuplicar() {
        var repo = new RepositorioSqlite(pasta.resolve("teste.db").toString());
        UUID id = UUID.randomUUID();
        repo.salvar(new Movimentacao(id, "Antiga", new BigDecimal("10.00"),
                TipoMovimentacao.DESPESA, Categoria.LAZER, LocalDate.of(2026, 1, 15)));
        repo.salvar(new Movimentacao(id, "Nova", new BigDecimal("20.50"),
                TipoMovimentacao.DESPESA, Categoria.LAZER, LocalDate.of(2026, 1, 16)));

        var lidas = repo.listar();

        assertEquals(1, lidas.size());
        assertEquals("Nova", lidas.get(0).getDescricao());
        assertEquals("20.50", lidas.get(0).getValor().toPlainString());
    }

    @Test
    void removerIdInexistenteRetornaFalse() {
        var repo = new RepositorioSqlite(pasta.resolve("teste.db").toString());
        assertFalse(repo.remover(UUID.randomUUID()));
    }
}