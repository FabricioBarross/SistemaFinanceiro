package com.sistemafinanceiro.model;
import  java.math.BigDecimal;

public class Movimentacao {

    private String descricao;
    private BigDecimal valor;
    private TipoMovimentacao tipo;
    private String categoria;

    public Movimentacao(
            String descricao,
            BigDecimal valor,
            TipoMovimentacao tipo,
            String categoria
    ) {
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
