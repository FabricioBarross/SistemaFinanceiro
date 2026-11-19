package com.sistemafinanceiro.service;

import com.sistemafinanceiro.model.Categoria;
import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.model.TipoMovimentacao;
import com.sistemafinanceiro.repository.RepositorioMovimentacao;

import javax.naming.spi.ObjectFactoryBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServicoFinanceiro {

    private final RepositorioMovimentacao repositorio;

    public ServicoFinanceiro(RepositorioMovimentacao repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio, "Repositório não pode ser nulo");
    }

    public void adicionar(Movimentacao movimentacao) {
        Objects.requireNonNull(movimentacao, "Movimentação não poder ser nula");
        repositorio.salvar(movimentacao);
    }

    public boolean remover(UUID id) {
        return repositorio.remover(id);
    }

    public List<Movimentacao> listar() {
        return repositorio.listar();
    }

    public boolean estaVazio() {
        return repositorio.listar().isEmpty();
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

    public List<Movimentacao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || fim.isBefore(inicio))
            throw new IllegalArgumentException("Período inválido");
        return repositorio.listar().stream()
                .filter(m -> !m.getData().isBefore(inicio) && !m.getData().isAfter(fim))
                .toList();
    }
    public Map<Categoria, BigDecimal> totalPorCategoria(TipoMovimentacao tipo) {
        return repositorio.listar().stream()
                .filter(m -> m.getTipo() == tipo)
                .collect(Collectors.groupingBy(
                        Movimentacao::getCategoria,
                        () -> new EnumMap<>(Categoria.class),
                        Collectors.reducing(BigDecimal.ZERO, Movimentacao::getValor, BigDecimal::add)));
    }

    private BigDecimal somarPorTipo(TipoMovimentacao tipo) {
        return repositorio.listar().stream()
                .filter(m -> m.getTipo() == tipo)
                .map(Movimentacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
