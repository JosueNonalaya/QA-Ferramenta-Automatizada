package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.enums.StatusAuditoria;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.Documento;
import br.com.autoaudit.model.Responsavel;
import br.com.autoaudit.model.Checklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO {

    public void salvar(Auditoria auditoria) {
        String sql = """
                INSERT INTO auditoria
                (
                    nome_projeto,
                    data_hora_agendada,
                    inicio,
                    fim,
                    status,
                    percentual_aderencia_aceitavel,
                    percentual_aderencia
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, auditoria.getNomeProjeto());
            statement.setString(
                    2,
                    converterData(auditoria.getDataHoraAgendada())
            );
            statement.setString(
                    3,
                    converterData(auditoria.getInicio())
            );
            statement.setString(
                    4,
                    converterData(auditoria.getFim())
            );
            statement.setString(5, auditoria.getStatus().name());
            statement.setDouble(
                    6,
                    auditoria.getPercentualAderenciaAceitavel()
            );
            statement.setDouble(
                    7,
                    auditoria.getPercentualAderencia()
            );

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    auditoria.setId(keys.getLong(1));
                }
            }

            vincularResponsaveis(connection, auditoria);
            vincularDocumentos(connection, auditoria);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar auditoria.", e);
        }
    }

    public Auditoria buscarPorId(Long id) {
        String sql = """
                SELECT id,
                       nome_projeto,
                       data_hora_agendada,
                       inicio,
                       fim,
                       status,
                       percentual_aderencia_aceitavel,
                       percentual_aderencia
                FROM auditoria
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    Auditoria auditoria = mapear(resultSet);

                    carregarResponsaveis(connection, auditoria);
                    carregarDocumentos(connection, auditoria);
                    carregarChecklist(connection, auditoria);

                    return auditoria;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar auditoria.", e);
        }

        return null;
    }

    public List<Auditoria> listarTodos() {
        String sql = """
                SELECT id,
                       nome_projeto,
                       data_hora_agendada,
                       inicio,
                       fim,
                       status,
                       percentual_aderencia_aceitavel,
                       percentual_aderencia
                FROM auditoria
                ORDER BY id
                """;

        List<Auditoria> auditorias = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Auditoria auditoria = mapear(resultSet);

                carregarResponsaveis(connection, auditoria);
                carregarDocumentos(connection, auditoria);
                carregarChecklist(connection, auditoria);

                auditorias.add(auditoria);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar auditorias.", e);
        }

        return auditorias;
    }

    public void atualizar(Auditoria auditoria) {
        String sql = """
                UPDATE auditoria
                SET nome_projeto = ?,
                    data_hora_agendada = ?,
                    inicio = ?,
                    fim = ?,
                    status = ?,
                    percentual_aderencia_aceitavel = ?,
                    percentual_aderencia = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, auditoria.getNomeProjeto());
            statement.setString(
                    2,
                    converterData(auditoria.getDataHoraAgendada())
            );
            statement.setString(
                    3,
                    converterData(auditoria.getInicio())
            );
            statement.setString(
                    4,
                    converterData(auditoria.getFim())
            );
            statement.setString(5, auditoria.getStatus().name());
            statement.setDouble(
                    6,
                    auditoria.getPercentualAderenciaAceitavel()
            );
            statement.setDouble(
                    7,
                    auditoria.getPercentualAderencia()
            );
            statement.setLong(8, auditoria.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar auditoria.", e);
        }
    }

    public void excluir(Long id) {

        String sqlResponsaveis = """
            DELETE FROM auditoria_responsavel
            WHERE auditoria_id = ?
            """;

        String sqlDocumentos = """
            DELETE FROM auditoria_documento
            WHERE auditoria_id = ?
            """;

        String sqlRespostas = """
            DELETE FROM resposta_checklist
            WHERE pergunta_id IN (
                SELECT pc.id
                FROM pergunta_checklist pc
                JOIN checklist c
                    ON c.id = pc.checklist_id
                WHERE c.auditoria_id = ?
            )
            """;

        String sqlPerguntas = """
            DELETE FROM pergunta_checklist
            WHERE checklist_id IN (
                SELECT id
                FROM checklist
                WHERE auditoria_id = ?
            )
            """;

        String sqlChecklist = """
            DELETE FROM checklist
            WHERE auditoria_id = ?
            """;

        String sqlNaoConformidades = """
            DELETE FROM nao_conformidade
            WHERE auditoria_id = ?
            """;

        String sqlAuditoria = """
            DELETE FROM auditoria
            WHERE id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            try {

                connection.setAutoCommit(false);

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlResponsaveis)) {

                    statement.setLong(1, id);
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlDocumentos)) {

                    statement.setLong(1, id);
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlRespostas)) {

                    statement.setLong(1, id);
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlPerguntas)) {

                    statement.setLong(1, id);
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlChecklist)) {

                    statement.setLong(1, id);
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlNaoConformidades)) {

                    statement.setLong(1, id);
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlAuditoria)) {

                    statement.setLong(1, id);
                    statement.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {

                connection.rollback();

                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir auditoria.",
                    e
            );
        }
    }

    public void adicionarResponsavel(
            Long auditoriaId,
            Long responsavelId
    ) {
        String sql = """
                INSERT INTO auditoria_responsavel
                (auditoria_id, responsavel_id)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, auditoriaId);
            statement.setLong(2, responsavelId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao adicionar responsável à auditoria.",
                    e
            );
        }
    }

    public void adicionarDocumento(
            Long auditoriaId,
            Long documentoId
    ) {
        String sql = """
                INSERT INTO auditoria_documento
                (auditoria_id, documento_id)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, auditoriaId);
            statement.setLong(2, documentoId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao adicionar documento à auditoria.",
                    e
            );
        }
    }

    private Auditoria mapear(ResultSet resultSet)
            throws SQLException {

        Auditoria auditoria = new Auditoria();

        auditoria.setId(resultSet.getLong("id"));
        auditoria.setNomeProjeto(resultSet.getString("nome_projeto"));

        auditoria.setDataHoraAgendada(
                converterData(resultSet.getString("data_hora_agendada"))
        );

        auditoria.setInicio(
                converterData(resultSet.getString("inicio"))
        );

        auditoria.setFim(
                converterData(resultSet.getString("fim"))
        );

        auditoria.setStatus(
                StatusAuditoria.valueOf(
                        resultSet.getString("status")
                )
        );

        auditoria.setPercentualAderenciaAceitavel(
                resultSet.getDouble(
                        "percentual_aderencia_aceitavel"
                )
        );

        auditoria.setPercentualAderencia(
                resultSet.getDouble("percentual_aderencia")
        );

        return auditoria;
    }

    private void vincularResponsaveis(
            Connection connection,
            Auditoria auditoria
    ) throws SQLException {

        String sql = """
                INSERT INTO auditoria_responsavel
                (auditoria_id, responsavel_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Responsavel responsavel : auditoria.getResponsaveis()) {

                if (responsavel.getId() == null) {
                    continue;
                }

                statement.setLong(1, auditoria.getId());
                statement.setLong(2, responsavel.getId());

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void vincularDocumentos(
            Connection connection,
            Auditoria auditoria
    ) throws SQLException {

        String sql = """
                INSERT INTO auditoria_documento
                (auditoria_id, documento_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Documento documento : auditoria.getDocumentos()) {

                if (documento.getId() == null) {
                    continue;
                }

                statement.setLong(1, auditoria.getId());
                statement.setLong(2, documento.getId());

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void carregarResponsaveis(
            Connection connection,
            Auditoria auditoria
    ) throws SQLException {

        String sql = """
                SELECT r.id
                FROM responsavel r
                JOIN auditoria_responsavel ar
                    ON ar.responsavel_id = r.id
                WHERE ar.auditoria_id = ?
                ORDER BY r.id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, auditoria.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                ResponsavelDAO responsavelDAO = new ResponsavelDAO();

                while (resultSet.next()) {
                    Responsavel responsavel =
                            responsavelDAO.buscarPorId(
                                    resultSet.getLong("id")
                            );

                    if (responsavel != null) {
                        auditoria.adicionarResponsavel(responsavel);
                    }
                }
            }
        }
    }

    private void carregarDocumentos(
            Connection connection,
            Auditoria auditoria
    ) throws SQLException {

        String sql = """
                SELECT d.id
                FROM documento d
                JOIN auditoria_documento ad
                    ON ad.documento_id = d.id
                WHERE ad.auditoria_id = ?
                ORDER BY d.id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, auditoria.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                DocumentoDAO documentoDAO = new DocumentoDAO();

                while (resultSet.next()) {
                    Documento documento =
                            documentoDAO.buscarPorId(
                                    resultSet.getLong("id")
                            );

                    if (documento != null) {
                        auditoria.adicionarDocumento(documento);
                    }
                }
            }
        }
    }

    private String converterData(LocalDateTime data) {
        return data == null ? null : data.toString();
    }

    private LocalDateTime converterData(String data) {
        return data == null ? null : LocalDateTime.parse(data);
    }

    private void carregarChecklist(
            Connection connection,
            Auditoria auditoria
    ) throws SQLException {

        String sql = """
            SELECT id, nome
            FROM checklist
            WHERE auditoria_id = ?
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, auditoria.getId());

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    Checklist checklist = new Checklist();

                    checklist.setId(
                            resultSet.getLong("id")
                    );

                    checklist.setNome(
                            resultSet.getString("nome")
                    );

                    checklist.setAuditoria(auditoria);

                    auditoria.setChecklist(checklist);
                }
            }
        }
    }
}