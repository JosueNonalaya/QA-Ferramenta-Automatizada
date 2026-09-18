package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.enums.StatusNotificacao;
import br.com.autoaudit.model.Notificacao;
import br.com.autoaudit.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO {

    public void salvar(Notificacao notificacao) {
        String sql = """
                INSERT INTO notificacao
                (mensagem, data_hora, status, usuario_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, notificacao.getMensagem());
            statement.setString(2, notificacao.getDataHora().toString());
            statement.setString(3, notificacao.getStatus().name());
            statement.setLong(4, notificacao.getUsuario().getId());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    notificacao.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar notificação.", e);
        }
    }

    public Notificacao buscarPorId(Long id) {
        String sql = """
                SELECT n.id,
                       n.mensagem,
                       n.data_hora,
                       n.status,
                       n.usuario_id,
                       u.nome,
                       u.email,
                       u.senha,
                       u.telefone
                FROM notificacao n
                JOIN usuario u ON u.id = n.usuario_id
                WHERE n.id = ?
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
            throw new RuntimeException("Erro ao buscar notificação.", e);
        }

        return null;
    }

    public List<Notificacao> listarTodos() {
        String sql = """
                SELECT n.id,
                       n.mensagem,
                       n.data_hora,
                       n.status,
                       n.usuario_id,
                       u.nome,
                       u.email,
                       u.senha,
                       u.telefone
                FROM notificacao n
                JOIN usuario u ON u.id = n.usuario_id
                ORDER BY n.id
                """;

        List<Notificacao> notificacoes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                notificacoes.add(mapear(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar notificações.", e);
        }

        return notificacoes;
    }

    public void atualizar(Notificacao notificacao) {
        String sql = """
                UPDATE notificacao
                SET mensagem = ?,
                    data_hora = ?,
                    status = ?,
                    usuario_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, notificacao.getMensagem());
            statement.setString(2, notificacao.getDataHora().toString());
            statement.setString(3, notificacao.getStatus().name());
            statement.setLong(4, notificacao.getUsuario().getId());
            statement.setLong(5, notificacao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar notificação.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM notificacao WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir notificação.", e);
        }
    }

    private Notificacao mapear(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(resultSet.getLong("usuario_id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setTelefone(resultSet.getString("telefone"));

        Notificacao notificacao = new Notificacao();

        notificacao.setId(resultSet.getLong("id"));
        notificacao.setMensagem(resultSet.getString("mensagem"));
        notificacao.setDataHora(
                LocalDateTime.parse(resultSet.getString("data_hora"))
        );
        notificacao.setStatus(
                StatusNotificacao.valueOf(resultSet.getString("status"))
        );
        notificacao.setUsuario(usuario);

        return notificacao;
    }
}