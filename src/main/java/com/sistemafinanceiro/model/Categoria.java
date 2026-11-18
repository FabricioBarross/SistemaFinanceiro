package com.sistemafinanceiro.model;

import java.util.List;
import java.util.Arrays;

public enum Categoria {

    SALARIO("Salário", TipoMovimentacao.RECEITA),
    RENDA_EXTRA("Renda extra", TipoMovimentacao.RECEITA),
    MORADIA("Moradia", TipoMovimentacao.DESPESA),
    ALIMENTACAO("Alimentação", TipoMovimentacao.DESPESA),
    TRANSPORTE("Transporte", TipoMovimentacao.DESPESA),
    SAUDE("Saúde", TipoMovimentacao.DESPESA),
    EDUCACAO("Educação", TipoMovimentacao.DESPESA),
    LAZER("Lazer", TipoMovimentacao.DESPESA),
    OUTROS("Outros", null);

    private final String nomeExibicao;
    private final TipoMovimentacao tipo;

    Categoria(String nomeExibicao, TipoMovimentacao tipo) {
        this.nomeExibicao = nomeExibicao;
        this.tipo = tipo;
    }
    public String getNomeExibicao() {
        return nomeExibicao;
    }
    public boolean aceita(TipoMovimentacao TipoMovimentacao) {
        return tipo == null || tipo == TipoMovimentacao;
    }
    public static List<Categoria> para(TipoMovimentacao TipoMovimentacao) {
        return Arrays.stream(values())
                .filter(c -> c.aceita(TipoMovimentacao))
                .toList();
    }
}