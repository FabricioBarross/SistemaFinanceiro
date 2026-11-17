package com.sistemafinanceiro.service;

import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.model.TipoMovimentacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ServicoFinanceiroTest {

    private ServicoFinanceiro servico;

    @BeforeEach
    void setUp() {
        servico = new ServicoFinanceiro();
    }

    private Movimentacao receita(String valor) {
        return new Movimentacao("Receita", new BigDecimal(valor),
                TipoMovimentacao.RECEITA, "Teste");
    }

    private Movimentacao despesa(String valor) {
        return new Movimentacao("Despesa", new BigDecimal(valor),
                TipoMovimentacao.DESPESA, "Teste");
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
}