package br.com.autoaudit.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() {
        try (
                Connection connection = DatabaseConnection.getConnection();
                InputStream inputStream = DatabaseInitializer.class
                        .getClassLoader()
                        .getResourceAsStream("database/schema.sql")
        ) {

            if (inputStream == null) {
                throw new IllegalStateException("Arquivo schema.sql não encontrado.");
            }

            StringBuilder sql = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String linha;

                while ((linha = reader.readLine()) != null) {
                    sql.append(linha).append("\n");
                }
            }

            String[] comandos = sql.toString().split(";");

            try (Statement statement = connection.createStatement()) {

                for (String comando : comandos) {
                    if (!comando.trim().isEmpty()) {
                        statement.execute(comando);
                    }
                }
            }

            System.out.println("Status: Banco SUCESSO");

        } catch (SQLException | IOException e) {
            System.err.println("Status: Banco ERRO inicializar");
            e.printStackTrace();
        }
    }
}