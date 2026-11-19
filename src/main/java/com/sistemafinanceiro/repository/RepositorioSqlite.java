package com.sistemafinanceiro.repository;

import com.sistemafinanceiro.model.Categoria;
import com.sistemafinanceiro.model.Movimentacao;
import com.sistemafinanceiro.model.TipoMovimentacao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioSqlite implements RepositorioMovimentacao {

    private final String url;

    public RepositorioSqlite(String caminhoArquivo) {
        this.url = "jdbc:sqlite:" + caminhoArquivo;
        criarTabela();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS movimentacao (
                    id TEXT PRIMARY KEY,
                    descricao TEXT NOT NULL,
                    valor_centavos INTEGER NOT NULL,
                    tipo TEXT NOT NULL,
                    categoria TEXT NOT NULL,
                    data TEXT NOT NULL
                )
                """;
        try (Connection c = conectar(); Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao criar a tabela", e);
        }
    }

    @Override
    public void salvar(Movimentacao m) {
        String sql = """
                INSERT INTO movimentacao (id, descricao, valor_centavos, tipo, categoria, data)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT(id) DO UPDATE SET
                    descricao = excluded.descricao,
                    valor_centavos = excluded.valor_centavos,
                    tipo = excluded.tipo,
                    categoria = excluded.categoria,
                    data = excluded.data
                """;
        try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getId().toString());
            ps.setString(2, m.getDescricao());
            ps.setLong(3, m.getValor().movePointRight(2).longValueExact());
            ps.setString(4, m.getTipo().name());
            ps.setString(5, m.getCategoria().name());
            ps.setString(6, m.getData().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao salvar movimentação", e);
        }
    }

    @Override
    public List<Movimentacao> listar() {
        String sql = "SELECT id, descricao, valor_centavos, tipo, categoria, data "
                + "FROM movimentacao ORDER BY rowid";
        List<Movimentacao> resultado = new ArrayList<>();
        try (Connection c = conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                resultado.add(new Movimentacao(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("descricao"),
                        BigDecimal.valueOf(rs.getLong("valor_centavos"), 2),
                        TipoMovimentacao.valueOf(rs.getString("tipo")),
                        Categoria.valueOf(rs.getString("categoria")),
                        LocalDate.parse(rs.getString("data"))));
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao listar movimentações", e);
        }
        return List.copyOf(resultado);
    }

    @Override
    public boolean remover(UUID id) {
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement("DELETE FROM movimentacao WHERE id = ?")) {
            ps.setString(1, id.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao remover movimentação", e);
        }
    }
}