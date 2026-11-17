package com.sistemafinanceiro.service;

import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.model.TipoMovimentacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ServicoFinanceiro {

    private final List<Movimentacao> movimentacoes = new ArrayList<>();

    public void adicionar(Movimentacao movimentacao) {
        Objects.requireNonNull(movimentacao, "Movimentação não pode ser nula");
        movimentacoes.add(movimentacao);
    }

    public List<Movimentacao> listar() {
        return List.copyOf(movimentacoes);
    }

    public boolean estaVazio() {
        return movimentacoes.isEmpty();
    }

    public BigDecimal totalReceitas() {
        return somarPorTipo(TipoMovimentacao.RECEITA);
    }

    public BigDecimal totalDespesas() {
        return somarPorTipo(TipoMovimentacao.DESPESA);
    }

    public BigDecimal saldo() {
        return totalReceitas().subtract(totalDespesas());
    }

    private BigDecimal somarPorTipo(TipoMovimentacao tipo) {
        return movimentacoes.stream()
                .filter(m -> m.getTipo() == tipo)
                .map(Movimentacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
