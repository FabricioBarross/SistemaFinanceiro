package com.sistemafinanceiro.repository;

import com.sistemafinanceiro.model.Movimentacao;
import java.util.List;
import java.util.UUID;

public interface RepositorioMovimentacao {
    void salvar(Movimentacao movimentacao);
    List<Movimentacao> listar();
    boolean remover(UUID id);
}
