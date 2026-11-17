package com.sistemafinanceiro.model;
import  java.math.BigDecimal;

public class Movimentacao {

    private final String descricao;
    private final BigDecimal valor;
    private final TipoMovimentacao tipo;
    private final String categoria;

    public Movimentacao(
            String descricao,
            BigDecimal valor,
            TipoMovimentacao tipo,
            String categoria
    ) {

        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição obrigatória");
        if (valor == null || valor.signum() < 0)
            throw new IllegalArgumentException("Valor não pode ser nulo ou negativo");
        if (tipo == null)
            throw new IllegalArgumentException("Tipo obrigatório");
        if (categoria == null || categoria.isBlank())
            throw new IllegalArgumentException("Categoria obrigatória");

        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
    }
    public String getDescricao() {
        return descricao;
    }
    public BigDecimal getValor() {
        return valor;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }
    public String getCategoria() {
        return categoria;
    }
}
