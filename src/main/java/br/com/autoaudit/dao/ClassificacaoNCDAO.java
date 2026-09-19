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

    // Salva uma nova classificação de não conformidade.
    public void salvar(ClassificacaoNC classificacao) {

        String sql = """
                INSERT INTO classificacao_nc
                (nome, descricao, prazo_horas)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, classificacao.getNome());
            statement.setString(2, classificacao.getDescricao());
            statement.setInt(3, classificacao.getPrazoHoras());

            statement.executeUpdate();

            // Recupera o ID gerado pelo banco e coloca no objeto.
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    classificacao.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao salvar classificação de NC.",
                    e
            );
        }
    }

    // Busca uma classificação pelo ID.
    public ClassificacaoNC buscarPorId(Long id) {

        String sql = """
                SELECT id,
                       nome,
                       descricao,
                       prazo_horas
                FROM classificacao_nc
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapear(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar classificação de NC.",
                    e
            );
        }

        return null;
    }

    // Retorna todas as classificações cadastradas.
    public List<ClassificacaoNC> listarTodos() {

        String sql = """
                SELECT id,
                       nome,
                       descricao,
                       prazo_horas
                FROM classificacao_nc
                ORDER BY id
                """;

        List<ClassificacaoNC> classificacoes =
                new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                classificacoes.add(mapear(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar classificações de NC.",
                    e
            );
        }

        return classificacoes;
    }

    // Atualiza os dados de uma classificação existente.
    public void atualizar(ClassificacaoNC classificacao) {

        String sql = """
                UPDATE classificacao_nc
                SET nome = ?,
                    descricao = ?,
                    prazo_horas = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, classificacao.getNome());
            statement.setString(2, classificacao.getDescricao());
            statement.setInt(3, classificacao.getPrazoHoras());
            statement.setLong(4, classificacao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar classificação de NC.",
                    e
            );
        }
    }

    // Exclui uma classificação pelo ID.
    public void excluir(Long id) {

        String sql = """
                DELETE FROM classificacao_nc
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir classificação de NC.",
                    e
            );
        }
    }

    // Converte uma linha do banco em um objeto ClassificacaoNC.
    private ClassificacaoNC mapear(
            ResultSet resultSet
    ) throws SQLException {

        ClassificacaoNC classificacao =
                new ClassificacaoNC();

        classificacao.setId(
                resultSet.getLong("id")
        );

        classificacao.setNome(
                resultSet.getString("nome")
        );

        classificacao.setDescricao(
                resultSet.getString("descricao")
        );

        classificacao.setPrazoHoras(
                resultSet.getInt("prazo_horas")
        );

        return classificacao;
    }
}