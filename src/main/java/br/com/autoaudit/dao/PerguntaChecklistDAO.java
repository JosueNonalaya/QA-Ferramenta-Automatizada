package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.model.Checklist;
import br.com.autoaudit.model.PerguntaChecklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerguntaChecklistDAO {

    public void salvar(PerguntaChecklist pergunta) {

        String sql = """
                INSERT INTO pergunta_checklist
                (descricao, ordem, checklist_id)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    pergunta.getDescricao()
            );

            statement.setInt(
                    2,
                    pergunta.getOrdem()
            );

            statement.setLong(
                    3,
                    pergunta.getChecklist().getId()
            );

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    pergunta.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao salvar pergunta do checklist.",
                    e
            );
        }
    }

    public PerguntaChecklist buscarPorId(Long id) {

        String sql = """
                SELECT id,
                       descricao,
                       ordem,
                       checklist_id
                FROM pergunta_checklist
                WHERE id = ?
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
            throw new RuntimeException(
                    "Erro ao buscar pergunta do checklist.",
                    e
            );
        }

        return null;
    }

    public List<PerguntaChecklist> listarTodos() {

        String sql = """
                SELECT id,
                       descricao,
                       ordem,
                       checklist_id
                FROM pergunta_checklist
                ORDER BY checklist_id, ordem
                """;

        List<PerguntaChecklist> perguntas = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                perguntas.add(mapear(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar perguntas do checklist.",
                    e
            );
        }

        return perguntas;
    }

    public List<PerguntaChecklist> listarPorChecklist(
            Long checklistId
    ) {

        String sql = """
                SELECT id,
                       descricao,
                       ordem,
                       checklist_id
                FROM pergunta_checklist
                WHERE checklist_id = ?
                ORDER BY ordem
                """;

        List<PerguntaChecklist> perguntas = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, checklistId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    perguntas.add(mapear(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar perguntas do checklist.",
                    e
            );
        }

        return perguntas;
    }

    public void atualizar(PerguntaChecklist pergunta) {

        String sql = """
                UPDATE pergunta_checklist
                SET descricao = ?,
                    ordem = ?,
                    checklist_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    pergunta.getDescricao()
            );

            statement.setInt(
                    2,
                    pergunta.getOrdem()
            );

            statement.setLong(
                    3,
                    pergunta.getChecklist().getId()
            );

            statement.setLong(
                    4,
                    pergunta.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar pergunta do checklist.",
                    e
            );
        }
    }

    public void excluir(Long id) {

        String sql = """
                DELETE FROM pergunta_checklist
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir pergunta do checklist.",
                    e
            );
        }
    }

    private PerguntaChecklist mapear(
            ResultSet resultSet
    ) throws SQLException {

        PerguntaChecklist pergunta =
                new PerguntaChecklist();

        pergunta.setId(
                resultSet.getLong("id")
        );

        pergunta.setDescricao(
                resultSet.getString("descricao")
        );

        pergunta.setOrdem(
                resultSet.getInt("ordem")
        );

        Long checklistId =
                resultSet.getLong("checklist_id");

        Checklist checklist = new Checklist();
        checklist.setId(checklistId);

        pergunta.setChecklist(checklist);

        return pergunta;
    }
}