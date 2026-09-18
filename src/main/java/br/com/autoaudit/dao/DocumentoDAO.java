package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.enums.TipoDocumento;
import br.com.autoaudit.model.Documento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {

    public void salvar(Documento documento) {
        String sql = """
                INSERT INTO documento (nome, caminho, tipo)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, documento.getNome());
            statement.setString(2, documento.getCaminho());
            statement.setString(3, documento.getTipo().name());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    documento.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar documento.", e);
        }
    }

    public Documento buscarPorId(Long id) {
        String sql = """
                SELECT id, nome, caminho, tipo
                FROM documento
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
            throw new RuntimeException("Erro ao buscar documento.", e);
        }

        return null;
    }

    public List<Documento> listarTodos() {
        String sql = """
                SELECT id, nome, caminho, tipo
                FROM documento
                ORDER BY id
                """;

        List<Documento> documentos = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                documentos.add(mapear(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar documentos.", e);
        }

        return documentos;
    }

    public void atualizar(Documento documento) {
        String sql = """
                UPDATE documento
                SET nome = ?, caminho = ?, tipo = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, documento.getNome());
            statement.setString(2, documento.getCaminho());
            statement.setString(3, documento.getTipo().name());
            statement.setLong(4, documento.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar documento.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM documento WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir documento.", e);
        }
    }

    private Documento mapear(ResultSet resultSet) throws SQLException {
        Documento documento = new Documento();

        documento.setId(resultSet.getLong("id"));
        documento.setNome(resultSet.getString("nome"));
        documento.setCaminho(resultSet.getString("caminho"));
        documento.setTipo(
                TipoDocumento.valueOf(resultSet.getString("tipo"))
        );

        return documento;
    }
}