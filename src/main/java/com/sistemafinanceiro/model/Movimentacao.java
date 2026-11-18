package com.sistemafinanceiro.model;

import  java.math.BigDecimal;
import  java.time.LocalDate;

public class Movimentacao {

    private final String descricao;
    private final BigDecimal valor;
    private final TipoMovimentacao tipo;
    private final Categoria categoria;
    private final LocalDate data;

    public Movimentacao(
            String descricao,
            BigDecimal valor,
            TipoMovimentacao tipo,
            Categoria categoria,
            LocalDate data
    ) {
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição obrigatória");
        if (valor == null || valor.signum() < 0)
            throw new IllegalArgumentException("Valor não pode ser nulo ou negativo");
        if (tipo == null)
            throw new IllegalArgumentException("Tipo obrigatório");
        if (categoria == null)
            throw new IllegalArgumentException("Categoria obrigatória");
        if (!categoria.aceita(tipo))
            throw new IllegalArgumentException("Categoria incompatível com o tipo");
        if (data == null)
            throw new IllegalArgumentException("Data obrigatória");

        this.descricao = descricao.trim();
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
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
    public Categoria getCategoria() {
        return categoria;
    }

    public LocalDate getData() {
        return data;
    }
}
