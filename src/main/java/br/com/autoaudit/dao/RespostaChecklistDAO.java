package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.enums.OpcaoResposta;
import br.com.autoaudit.model.PerguntaChecklist;
import br.com.autoaudit.model.RespostaChecklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RespostaChecklistDAO {

    // Salva uma nova resposta do checklist.
    public void salvar(RespostaChecklist resposta) {

        String sql = """
                INSERT INTO resposta_checklist
                (
                    opcao,
                    pergunta_id,
                    data_hora_resposta
                )
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    resposta.getOpcao().name()
            );

            statement.setLong(
                    2,
                    resposta.getPergunta().getId()
            );

            statement.setString(
                    3,
                    resposta.getDataHoraResposta().toString()
            );

            statement.executeUpdate();

            // Recupera o ID gerado pelo banco.
            try (ResultSet keys = statement.getGeneratedKeys()) {

                if (keys.next()) {
                    resposta.setId(
                            keys.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao salvar resposta do checklist.",
                    e
            );
        }
    }

    // Busca uma resposta pelo ID.
    public RespostaChecklist buscarPorId(Long id) {

        String sql = """
                SELECT
                    r.id,
                    r.opcao,
                    r.data_hora_resposta,
                    p.id AS pergunta_id,
                    p.descricao,
                    p.ordem
                FROM resposta_checklist r
                JOIN pergunta_checklist p
                    ON p.id = r.pergunta_id
                WHERE r.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapear(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar resposta do checklist.",
                    e
            );
        }

        return null;
    }

    // Retorna todas as respostas cadastradas.
    public List<RespostaChecklist> listarTodos() {

        String sql = """
                SELECT
                    r.id,
                    r.opcao,
                    r.data_hora_resposta,
                    p.id AS pergunta_id,
                    p.descricao,
                    p.ordem
                FROM resposta_checklist r
                JOIN pergunta_checklist p
                    ON p.id = r.pergunta_id
                ORDER BY r.id
                """;

        List<RespostaChecklist> respostas =
                new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                respostas.add(
                        mapear(resultSet)
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar respostas do checklist.",
                    e
            );
        }

        return respostas;
    }

    // Retorna as respostas pertencentes a uma pergunta.
    public List<RespostaChecklist> listarPorPergunta(
            Long perguntaId
    ) {

        String sql = """
                SELECT
                    r.id,
                    r.opcao,
                    r.data_hora_resposta,
                    p.id AS pergunta_id,
                    p.descricao,
                    p.ordem
                FROM resposta_checklist r
                JOIN pergunta_checklist p
                    ON p.id = r.pergunta_id
                WHERE r.pergunta_id = ?
                ORDER BY r.id
                """;

        List<RespostaChecklist> respostas =
                new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, perguntaId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    respostas.add(
                            mapear(resultSet)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar respostas da pergunta.",
                    e
            );
        }

        return respostas;
    }

    // Atualiza uma resposta existente.
    public void atualizar(RespostaChecklist resposta) {

        String sql = """
                UPDATE resposta_checklist
                SET opcao = ?,
                    pergunta_id = ?,
                    data_hora_resposta = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    resposta.getOpcao().name()
            );

            statement.setLong(
                    2,
                    resposta.getPergunta().getId()
            );

            statement.setString(
                    3,
                    resposta.getDataHoraResposta().toString()
            );

            statement.setLong(
                    4,
                    resposta.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar resposta do checklist.",
                    e
            );
        }
    }

    // Exclui uma resposta pelo ID.
    public void excluir(Long id) {

        String sql = """
                DELETE FROM resposta_checklist
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir resposta do checklist.",
                    e
            );
        }
    }

    // Converte uma linha do banco em um objeto RespostaChecklist.
    private RespostaChecklist mapear(
            ResultSet resultSet
    ) throws SQLException {

        PerguntaChecklist pergunta =
                new PerguntaChecklist();

        pergunta.setId(
                resultSet.getLong("pergunta_id")
        );

        pergunta.setDescricao(
                resultSet.getString("descricao")
        );

        pergunta.setOrdem(
                resultSet.getInt("ordem")
        );

        RespostaChecklist resposta =
                new RespostaChecklist();

        resposta.setId(
                resultSet.getLong("id")
        );

        resposta.setOpcao(
                OpcaoResposta.valueOf(
                        resultSet.getString("opcao")
                )
        );

        resposta.setPergunta(
                pergunta
        );

        resposta.setDataHoraResposta(
                LocalDateTime.parse(
                        resultSet.getString(
                                "data_hora_resposta"
                        )
                )
        );

        return resposta;
    }
}