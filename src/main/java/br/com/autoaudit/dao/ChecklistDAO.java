package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.Checklist;
import br.com.autoaudit.model.PerguntaChecklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChecklistDAO {

    public void salvar(Checklist checklist) {
        String sql = """
                INSERT INTO checklist (nome, auditoria_id)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, checklist.getNome());

            if (checklist.getAuditoria() != null
                    && checklist.getAuditoria().getId() != null) {
                statement.setLong(
                        2,
                        checklist.getAuditoria().getId()
                );
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    checklist.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar checklist.", e);
        }
    }

    public Checklist buscarPorId(Long id) {
        String sql = """
                SELECT id, nome, auditoria_id
                FROM checklist
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    Checklist checklist = mapear(resultSet);

                    carregarPerguntas(
                            connection,
                            checklist
                    );

                    return checklist;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar checklist.", e);
        }

        return null;
    }

    public List<Checklist> listarTodos() {
        String sql = """
                SELECT id, nome, auditoria_id
                FROM checklist
                ORDER BY id
                """;

        List<Checklist> checklists = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Checklist checklist = mapear(resultSet);

                carregarPerguntas(
                        connection,
                        checklist
                );

                checklists.add(checklist);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar checklists.", e);
        }

        return checklists;
    }

    public void atualizar(Checklist checklist) {
        String sql = """
                UPDATE checklist
                SET nome = ?,
                    auditoria_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, checklist.getNome());

            if (checklist.getAuditoria() != null
                    && checklist.getAuditoria().getId() != null) {
                statement.setLong(
                        2,
                        checklist.getAuditoria().getId()
                );
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }

            statement.setLong(3, checklist.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar checklist.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM checklist WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir checklist.", e);
        }
    }

    private Checklist mapear(ResultSet resultSet) throws SQLException {
        Checklist checklist = new Checklist();

        checklist.setId(resultSet.getLong("id"));
        checklist.setNome(resultSet.getString("nome"));

        Long auditoriaId = (Long) resultSet.getObject("auditoria_id");

        if (auditoriaId != null) {
            Auditoria auditoria = new Auditoria();
            auditoria.setId(auditoriaId);
            checklist.setAuditoria(auditoria);
        }

        return checklist;
    }

    private void carregarPerguntas(
            Connection connection,
            Checklist checklist
    ) throws SQLException {

        String sql = """
                SELECT id, descricao, ordem
                FROM pergunta_checklist
                WHERE checklist_id = ?
                ORDER BY ordem
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, checklist.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    PerguntaChecklist pergunta = new PerguntaChecklist();

                    pergunta.setId(resultSet.getLong("id"));
                    pergunta.setDescricao(
                            resultSet.getString("descricao")
                    );
                    pergunta.setOrdem(
                            resultSet.getInt("ordem")
                    );

                    pergunta.setChecklist(checklist);

                    checklist.adicionarPergunta(pergunta);
                }
            }
        }
    }
}