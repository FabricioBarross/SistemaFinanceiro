package com.sistemafinanceiro.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Movimentacao {

    private final UUID id;
    private final String descricao;
    private final BigDecimal valor;
    private final TipoMovimentacao tipo;
    private final Categoria categoria;
    private final LocalDate data;

    public Movimentacao(String descricao, BigDecimal valor, TipoMovimentacao tipo, Categoria categoria, LocalDate data) {
        this(UUID.randomUUID(), descricao, valor, tipo, categoria, data);
    }

    public Movimentacao(UUID id, String descricao, BigDecimal valor, TipoMovimentacao tipo, Categoria categoria, LocalDate data) {

        if (id == null)
            throw new IllegalArgumentException("Id obrigatório");
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição obrigatória");
        if (tipo == null)
            throw new IllegalArgumentException("Tipo obrigatório");
        if (categoria == null)
            throw new IllegalArgumentException("Categoria obrigatória");
        if (!categoria.aceita(tipo))
            throw new IllegalArgumentException("Categoria incompatível com o tipo");
        if (data == null)
            throw new IllegalArgumentException("Data obrigatória");

        this.id = id;
        this.descricao = descricao.trim();
        this.valor = normalizarValor(valor);
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
    }

    public static BigDecimal normalizarValor(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0)
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        if (valor.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("Valor aceita no máximo duas casa decimais");
        return valor.setScale(2, RoundingMode.UNNECESSARY);
    }

    public UUID getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Movimentacao m && id.equals(m.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
