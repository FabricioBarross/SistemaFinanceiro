package com.sistemafinanceiro.repository;

import com.sistemafinanceiro.model.Movimentacao;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.List;
import java.util.Map;

public class RepositorioEmMemoria implements RepositorioMovimentacao {

    private final Map<UUID, Movimentacao> dados = new LinkedHashMap<>();

    @Override
    public void salvar(Movimentacao m) {
        dados.put(m.getId(), m);
    }

    @Override
    public List<Movimentacao> listar() {
        return List.copyOf(dados.values());
    }

    @Override
    public boolean remover(UUID id) {
        return dados.remove(id) != null;
    }

}
