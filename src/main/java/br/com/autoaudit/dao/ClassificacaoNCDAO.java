package br.com.autoaudit.dao;

import br.com.autoaudit.database.DatabaseConnection;
import br.com.autoaudit.model.ClassificacaoNC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassificacaoNCDAO {

    public void salvar(ClassificacaoNC classificacao) {
        String sql = """
                INSERT INTO classificacao_nc (nome, descricao)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, classificacao.getNome());
            statement.setString(2, classificacao.getDescricao());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    classificacao.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar classificação de NC.", e);
        }
    }

    public ClassificacaoNC buscarPorId(Long id) {
        String sql = """
                SELECT id, nome, descricao
                FROM classificacao_nc
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
            throw new RuntimeException("Erro ao buscar classificação de NC.", e);
        }

        return null;
    }

    public List<ClassificacaoNC> listarTodos() {
        String sql = """
                SELECT id, nome, descricao
                FROM classificacao_nc
                ORDER BY id
                """;

        List<ClassificacaoNC> classificacoes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                classificacoes.add(mapear(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar classificações de NC.", e);
        }

        return classificacoes;
    }

    public void atualizar(ClassificacaoNC classificacao) {
        String sql = """
                UPDATE classificacao_nc
                SET nome = ?, descricao = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, classificacao.getNome());
            statement.setString(2, classificacao.getDescricao());
            statement.setLong(3, classificacao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar classificação de NC.", e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM classificacao_nc WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir classificação de NC.", e);
        }
    }

    private ClassificacaoNC mapear(ResultSet resultSet) throws SQLException {
        ClassificacaoNC classificacao = new ClassificacaoNC();

        classificacao.setId(resultSet.getLong("id"));
        classificacao.setNome(resultSet.getString("nome"));
        classificacao.setDescricao(resultSet.getString("descricao"));

        return classificacao;
    }
}