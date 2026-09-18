package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.enums.StatusNC;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.ClassificacaoNC;
import br.com.autoaudit.model.NaoConformidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NaoConformidadeDAO {

    public void salvar(NaoConformidade naoConformidade) {
        String sql = """
                INSERT INTO nao_conformidade
                (descricao, status, classificacao_id, auditoria_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, naoConformidade.getDescricao());
            statement.setString(2, naoConformidade.getStatus().name());
            statement.setLong(
                    3,
                    naoConformidade.getClassificacao().getId()
            );
            statement.setLong(
                    4,
                    naoConformidade.getAuditoria().getId()
            );

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    naoConformidade.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar não conformidade.", e);
        }
    }

    public NaoConformidade buscarPorId(Long id) {
        String sql = """
                SELECT nc.id,
                       nc.descricao,
                       nc.status,
                       nc.classificacao_id,
                       c.nome AS classificacao_nome,
                       c.descricao AS classificacao_descricao,
                       nc.auditoria_id
                FROM nao_conformidade nc
                JOIN classificacao_nc c
                    ON c.id = nc.classificacao_id
                WHERE nc.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapear(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar não conformidade.", e);
        }

        return null;
    }

    public List<NaoConformidade> listarTodos() {
        String sql = """
                SELECT nc.id,
                       nc.descricao,
                       nc.status,
                       nc.classificacao_id,
                       c.nome AS classificacao_nome,
                       c.descricao AS classificacao_descricao,
                       nc.auditoria_id
                FROM nao_conformidade nc
                JOIN classificacao_nc c
                    ON c.id = nc.classificacao_id
                ORDER BY nc.id
                """;

        List<NaoConformidade> naoConformidades = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                naoConformidades.add(mapear(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar não conformidades.", e);
        }

        return naoConformidades;
    }

    public void atualizar(NaoConformidade naoConformidade) {
        String sql = """
                UPDATE nao_conformidade
                SET descricao = ?,
                    status = ?,
                    classificacao_id = ?,
                    auditoria_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, naoConformidade.getDescricao());
            statement.setString(2, naoConformidade.getStatus().name());
            statement.setLong(
                    3,
                    naoConformidade.getClassificacao().getId()
            );
            statement.setLong(
                    4,
                    naoConformidade.getAuditoria().getId()
            );
            statement.setLong(5, naoConformidade.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar não conformidade.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM nao_conformidade WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir não conformidade.", e);
        }
    }

    private NaoConformidade mapear(ResultSet resultSet) throws SQLException {
        ClassificacaoNC classificacao = new ClassificacaoNC();

        classificacao.setId(resultSet.getLong("classificacao_id"));
        classificacao.setNome(resultSet.getString("classificacao_nome"));
        classificacao.setDescricao(
                resultSet.getString("classificacao_descricao")
        );

        Auditoria auditoria = new Auditoria();
        auditoria.setId(resultSet.getLong("auditoria_id"));

        NaoConformidade naoConformidade = new NaoConformidade();

        naoConformidade.setId(resultSet.getLong("id"));
        naoConformidade.setDescricao(resultSet.getString("descricao"));
        naoConformidade.setStatus(
                StatusNC.valueOf(resultSet.getString("status"))
        );
        naoConformidade.setClassificacao(classificacao);
        naoConformidade.setAuditoria(auditoria);

        return naoConformidade;
    }
}