package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.model.Responsavel;
import br.com.autoaudit.model.Usuario;
import br.com.autoaudit.enums.TipoResponsavel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO {

    public void salvar(Responsavel responsavel) {
        String sql = """
                INSERT INTO responsavel (usuario_id, tipo)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, responsavel.getUsuario().getId());
            statement.setString(2, responsavel.getTipo().name());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    responsavel.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar responsável.", e);
        }
    }

    public Responsavel buscarPorId(Long id) {
        String sql = """
                SELECT r.id,
                       r.usuario_id,
                       r.tipo,
                       u.nome,
                       u.email,
                       u.senha,
                       u.telefone
                FROM responsavel r
                JOIN usuario u ON u.id = r.usuario_id
                WHERE r.id = ?
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
            throw new RuntimeException("Erro ao buscar responsável.", e);
        }

        return null;
    }

    public List<Responsavel> listarTodos() {
        String sql = """
                SELECT r.id,
                       r.usuario_id,
                       r.tipo,
                       u.nome,
                       u.email,
                       u.senha,
                       u.telefone
                FROM responsavel r
                JOIN usuario u ON u.id = r.usuario_id
                ORDER BY r.id
                """;

        List<Responsavel> responsaveis = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                responsaveis.add(mapear(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar responsáveis.", e);
        }

        return responsaveis;
    }

    public void atualizar(Responsavel responsavel) {
        String sql = """
                UPDATE responsavel
                SET usuario_id = ?, tipo = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, responsavel.getUsuario().getId());
            statement.setString(2, responsavel.getTipo().name());
            statement.setLong(3, responsavel.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar responsável.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM responsavel WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir responsável.", e);
        }
    }

    private Responsavel mapear(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(resultSet.getLong("usuario_id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setTelefone(resultSet.getString("telefone"));

        Responsavel responsavel = new Responsavel();

        responsavel.setId(resultSet.getLong("id"));
        responsavel.setUsuario(usuario);
        responsavel.setTipo(
                TipoResponsavel.valueOf(resultSet.getString("tipo"))
        );

        return responsavel;
    }
}