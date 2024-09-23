package org.bazarPet.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // URL do banco de dados MySQL
    // private static final String URL = "jdbc:mysql://localhost:3306/inventory_db?useSSL=false&serverTimezone=UTC";

    // URL do banco de dados SQLite
    private static final String URL = "jdbc:sqlite:src/main/resources/inventory.db";

    // Necessário para o banco de dados MySQL
    private static final String USER = "root";  // Substitua com o usuário do banco
    private static final String PASSWORD = "password";  // Substitua com a senha do banco

    // Métdo para obter a conexão
    public static Connection connect() throws SQLException {
        Connection connection;
        try {
            // Inicializando o driver MySQL do JDBC
            // Class.forName("com.mysql.cj.jdbc.Driver");

            // Inicializando o driver SQLite do JDBC
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão realizada com sucesso.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    // Métdo para fechar a conexão
    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}

