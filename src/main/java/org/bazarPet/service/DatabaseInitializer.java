package org.bazarPet.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    // Inicializa o banco de dados SQLite. Se ele ainda não existir, cria o banco de dados
    private static final String DB_PATH = "src/main/resources/inventory.db";
    private static final String SCHEMA_PATH = "src/main/resources/schemaLite.sql";

    public static void initializeDatabase(Connection conn) {
        File dbFile = new File(DB_PATH);

        try (Statement stmt = conn.createStatement()) {

            // Verifica se a tabela InventoryItem já existe
            if (!doesTableExist(conn, "InventoryItem")) {
                // Se a tabela não existir, cria as tabelas a partir do script schema.sql
                String sql = new String(Files.readAllBytes(Paths.get(SCHEMA_PATH)));
                stmt.executeUpdate(sql);
                System.out.println("Banco de dados e tabelas criadas com sucesso!");
            } else {
                System.out.println("Tabelas já existem, nenhuma ação necessária.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Verifica se uma tabela existe no banco de dados
    private static boolean doesTableExist(Connection conn, String tableName) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet res = meta.getTables(null, null, tableName, new String[]{"TABLE"});
            return res.next();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar a existência da tabela: " + tableName, e);
        }
    }
    // Inicia o banco de dados MySQL. Se ele ainda não existir, cria o banco de dados
    /*
        private static final String DB_NAME = "inventory_db";
        private static final String SCHEMA_PATH = "src/main/resources/schema.sql";

        public static void initializeDatabase(Connection conn) {
            try (Statement stmt = conn.createStatement()) {

                // Verifica se o banco de dados já existe
                ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE '" + DB_NAME + "'");
                if (rs.next()) {
                    System.out.println("Banco de dados '" + DB_NAME + "' já existe, nenhuma ação necessária.");
                } else {
                    // Cria o banco de dados se não existir
                    stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
                    System.out.println("Banco de dados '" + DB_NAME + "' criado com sucesso!");

                    // Use o banco de dados recém-criado
                    stmt.executeUpdate("USE " + DB_NAME);

                    // Lê e executa o script schema.sql para criar as tabelas
                    String sql = new String(Files.readAllBytes(Paths.get(SCHEMA_PATH)));
                    stmt.executeUpdate(sql);
                    System.out.println("Tabelas criadas com sucesso!");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    */
}
