package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.enums.StatusNC;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.ClassificacaoNC;
import br.com.autoaudit.model.NaoConformidade;
import br.com.autoaudit.model.Responsavel;
import br.com.autoaudit.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NaoConformidadeDAO {

    // Salva uma nova não conformidade.
    public void salvar(NaoConformidade naoConformidade) {

        String sql = """
                INSERT INTO nao_conformidade
                (
                    descricao,
                    status,
                    classificacao_id,
                    auditoria_id,
                    data_identificacao,
                    responsavel_id,
                    data_resolucao,
                    data_escalonamento,
                    acao_corretiva
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    naoConformidade.getDescricao()
            );

            statement.setString(
                    2,
                    naoConformidade.getStatus().name()
            );

            statement.setLong(
                    3,
                    naoConformidade.getClassificacao().getId()
            );

            statement.setLong(
                    4,
                    naoConformidade.getAuditoria().getId()
            );

            setLocalDate(
                    statement,
                    5,
                    naoConformidade.getDataIdentificacao()
            );

            // O responsável pode ainda não ter sido definido.
            if (naoConformidade.getResponsavel() != null
                    && naoConformidade.getResponsavel().getId() != null) {

                statement.setLong(
                        6,
                        naoConformidade.getResponsavel().getId()
                );

            } else {
                statement.setNull(
                        6,
                        java.sql.Types.INTEGER
                );
            }

            setLocalDate(
                    statement,
                    7,
                    naoConformidade.getDataResolucao()
            );

            setLocalDate(
                    statement,
                    8,
                    naoConformidade.getDataEscalonamento()
            );

            statement.setString(
                    9,
                    naoConformidade.getAcaoCorretiva()
            );

            statement.executeUpdate();

            // Recupera o ID gerado pelo banco.
            try (ResultSet keys = statement.getGeneratedKeys()) {

                if (keys.next()) {
                    naoConformidade.setId(
                            keys.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao salvar não conformidade.",
                    e
            );
        }
    }

    // Busca uma não conformidade pelo ID.
    public NaoConformidade buscarPorId(Long id) {

        String sql = """
                SELECT
                    nc.id,
                    nc.descricao,
                    nc.status,
                    nc.classificacao_id,
                    c.nome AS classificacao_nome,
                    c.descricao AS classificacao_descricao,
                    c.prazo_horas AS classificacao_prazo_horas,
                    nc.auditoria_id,
                    nc.data_identificacao,
                    nc.responsavel_id,
                    nc.data_resolucao,
                    nc.data_escalonamento,
                    nc.acao_corretiva,
                    u.id AS usuario_id,
                    u.nome AS usuario_nome,
                    u.email AS usuario_email,
                    u.senha AS usuario_senha,
                    u.telefone AS usuario_telefone,
                    r.tipo AS responsavel_tipo
                FROM nao_conformidade nc
                JOIN classificacao_nc c
                    ON c.id = nc.classificacao_id
                LEFT JOIN responsavel r
                    ON r.id = nc.responsavel_id
                LEFT JOIN usuario u
                    ON u.id = r.usuario_id
                WHERE nc.id = ?
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
                    "Erro ao buscar não conformidade.",
                    e
            );
        }

        return null;
    }

    // Retorna todas as não conformidades cadastradas.
    public List<NaoConformidade> listarTodos() {

        String sql = """
                SELECT
                    nc.id,
                    nc.descricao,
                    nc.status,
                    nc.classificacao_id,
                    c.nome AS classificacao_nome,
                    c.descricao AS classificacao_descricao,
                    c.prazo_horas AS classificacao_prazo_horas,
                    nc.auditoria_id,
                    nc.data_identificacao,
                    nc.responsavel_id,
                    nc.data_resolucao,
                    nc.data_escalonamento,
                    nc.acao_corretiva,
                    u.id AS usuario_id,
                    u.nome AS usuario_nome,
                    u.email AS usuario_email,
                    u.senha AS usuario_senha,
                    u.telefone AS usuario_telefone,
                    r.tipo AS responsavel_tipo
                FROM nao_conformidade nc
                JOIN classificacao_nc c
                    ON c.id = nc.classificacao_id
                LEFT JOIN responsavel r
                    ON r.id = nc.responsavel_id
                LEFT JOIN usuario u
                    ON u.id = r.usuario_id
                ORDER BY nc.id
                """;

        List<NaoConformidade> naoConformidades =
                new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                naoConformidades.add(
                        mapear(resultSet)
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar não conformidades.",
                    e
            );
        }

        return naoConformidades;
    }

    // Atualiza uma não conformidade existente.
    public void atualizar(NaoConformidade naoConformidade) {

        String sql = """
                UPDATE nao_conformidade
                SET descricao = ?,
                    status = ?,
                    classificacao_id = ?,
                    auditoria_id = ?,
                    data_identificacao = ?,
                    responsavel_id = ?,
                    data_resolucao = ?,
                    data_escalonamento = ?,
                    acao_corretiva = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    naoConformidade.getDescricao()
            );

            statement.setString(
                    2,
                    naoConformidade.getStatus().name()
            );

            statement.setLong(
                    3,
                    naoConformidade.getClassificacao().getId()
            );

            statement.setLong(
                    4,
                    naoConformidade.getAuditoria().getId()
            );

            setLocalDate(
                    statement,
                    5,
                    naoConformidade.getDataIdentificacao()
            );

            if (naoConformidade.getResponsavel() != null
                    && naoConformidade.getResponsavel().getId() != null) {

                statement.setLong(
                        6,
                        naoConformidade.getResponsavel().getId()
                );

            } else {
                statement.setNull(
                        6,
                        java.sql.Types.INTEGER
                );
            }

            setLocalDate(
                    statement,
                    7,
                    naoConformidade.getDataResolucao()
            );

            setLocalDate(
                    statement,
                    8,
                    naoConformidade.getDataEscalonamento()
            );

            statement.setString(
                    9,
                    naoConformidade.getAcaoCorretiva()
            );

            statement.setLong(
                    10,
                    naoConformidade.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar não conformidade.",
                    e
            );
        }
    }

    // Exclui uma não conformidade pelo ID.
    public void excluir(Long id) {

        String sql = """
                DELETE FROM nao_conformidade
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir não conformidade.",
                    e
            );
        }
    }

    // Converte uma linha do banco em um objeto NaoConformidade.
    private NaoConformidade mapear(
            ResultSet resultSet
    ) throws SQLException {

        ClassificacaoNC classificacao =
                new ClassificacaoNC();

        classificacao.setId(
                resultSet.getLong("classificacao_id")
        );

        classificacao.setNome(
                resultSet.getString("classificacao_nome")
        );

        classificacao.setDescricao(
                resultSet.getString(
                        "classificacao_descricao"
                )
        );

        classificacao.setPrazoHoras(
                resultSet.getInt(
                        "classificacao_prazo_horas"
                )
        );

        Auditoria auditoria =
                new Auditoria();

        auditoria.setId(
                resultSet.getLong("auditoria_id")
        );

        NaoConformidade naoConformidade =
                new NaoConformidade();

        naoConformidade.setId(
                resultSet.getLong("id")
        );

        naoConformidade.setDescricao(
                resultSet.getString("descricao")
        );

        naoConformidade.setStatus(
                StatusNC.valueOf(
                        resultSet.getString("status")
                )
        );

        naoConformidade.setClassificacao(
                classificacao
        );

        naoConformidade.setAuditoria(
                auditoria
        );

        naoConformidade.setDataIdentificacao(
                getLocalDate(
                        resultSet,
                        "data_identificacao"
                )
        );

        // Verifica se existe um responsável associado.
        Long responsavelId =
                getLongNullable(
                        resultSet,
                        "responsavel_id"
                );

        if (responsavelId != null) {

            Usuario usuario = new Usuario();

            usuario.setId(
                    resultSet.getLong("usuario_id")
            );

            usuario.setNome(
                    resultSet.getString("usuario_nome")
            );

            usuario.setEmail(
                    resultSet.getString("usuario_email")
            );

            usuario.setSenha(
                    resultSet.getString("usuario_senha")
            );

            usuario.setTelefone(
                    resultSet.getString("usuario_telefone")
            );

            Responsavel responsavel =
                    new Responsavel();

            responsavel.setId(
                    responsavelId
            );

            responsavel.setUsuario(
                    usuario
            );

            String tipo =
                    resultSet.getString(
                            "responsavel_tipo"
                    );

            if (tipo != null) {
                responsavel.setTipo(
                        br.com.autoaudit.enums.TipoResponsavel
                                .valueOf(tipo)
                );
            }

            naoConformidade.setResponsavel(
                    responsavel
            );
        }

        naoConformidade.setDataResolucao(
                getLocalDate(
                        resultSet,
                        "data_resolucao"
                )
        );

        naoConformidade.setDataEscalonamento(
                getLocalDate(
                        resultSet,
                        "data_escalonamento"
                )
        );

        naoConformidade.setAcaoCorretiva(
                resultSet.getString(
                        "acao_corretiva"
                )
        );

        return naoConformidade;
    }

    // Define uma data no PreparedStatement ou NULL.
    private void setLocalDate(
            PreparedStatement statement,
            int indice,
            LocalDate data
    ) throws SQLException {

        if (data != null) {
            statement.setString(
                    indice,
                    data.toString()
            );
        } else {
            statement.setNull(
                    indice,
                    java.sql.Types.VARCHAR
            );
        }
    }

    // Recupera uma data do banco tratando valores NULL.
    private LocalDate getLocalDate(
            ResultSet resultSet,
            String coluna
    ) throws SQLException {

        String valor =
                resultSet.getString(coluna);

        if (valor == null) {
            return null;
        }

        return LocalDate.parse(valor);
    }

    // Recupera um Long tratando valores NULL.
    private Long getLongNullable(
            ResultSet resultSet,
            String coluna
    ) throws SQLException {

        long valor =
                resultSet.getLong(coluna);

        if (resultSet.wasNull()) {
            return null;
        }

        return valor;
    }
}