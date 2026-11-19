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

    public boolean aceita(TipoMovimentacao TipoMov) {

        return tipo == null || tipo == TipoMov;
    }

    public static List<Categoria> para(TipoMovimentacao TipoMov) {
        return Arrays.stream(values())
                .filter(c -> c.aceita(TipoMov))
                .toList();
    }
}