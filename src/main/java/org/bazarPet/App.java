package org.bazarPet;

import org.bazarPet.controller.DonorController;
import org.bazarPet.controller.InventoryController;
import org.bazarPet.model.Donor;
import org.bazarPet.model.InventoryItem;
import org.bazarPet.repository.jdbc.DatabaseConnection;
import org.bazarPet.repository.jdbc.DonorJdbcRepository;
import org.bazarPet.repository.jdbc.InventoryItemJdbcRepository;
import org.bazarPet.service.DatabaseInitializer;
import org.bazarPet.service.DonorService;
import org.bazarPet.service.InventoryService;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        // Inicializa a conexão com o banco de dados
        Connection connection = null;

        try {
            connection = DatabaseConnection.connect();
            System.out.println("Checando a existência do banco de dados e tabel:");
            DatabaseInitializer.initializeDatabase(connection);

            // Instanciar o repositório e serviço do doador (JDBC neste caso)
            DonorJdbcRepository donorRepository = new DonorJdbcRepository(connection);
            DonorService donorService = new DonorService(donorRepository);

            // Instanciar o repositório e serviço do inventário (JDBC neste caso)
            InventoryItemJdbcRepository inventoryRepository = new InventoryItemJdbcRepository(connection, donorRepository);
            InventoryService inventoryService = new InventoryService(inventoryRepository);

            // Instanciar o controlador passando o serviço
            DonorController donorController = new DonorController(donorService);
            InventoryController inventoryController = new InventoryController(inventoryService, donorService);

            // Criar um novo doador
            Donor newDonor1 = new Donor("Teresinha", "teh@email.com", "03299993333", null);
            Donor newDonor2 = new Donor("Bruno", "bruh@email.com", "03299996666", null);

            // Adiciona um novo doador usando o DonorController
            donorController.addDonor(newDonor1);
            donorController.addDonor(newDonor2);

            // Criar um novo item para o inventário
            InventoryItem newItem0 = new InventoryItem("Camiseta", 25.00f, 1, newDonor1);
            InventoryItem newItem1 = new InventoryItem("Calça", 50.00f, 1, newDonor2);
            InventoryItem newItem2 = new InventoryItem("Acessórios", 80.00f, 1, null);


            // Adiciona o item usando o InventoryController
            inventoryController.addItem(newItem0);
            inventoryController.addItem(newItem1);
            inventoryController.addItem(newItem2);

            // System.out.println("Item adicionado com sucesso ao inventário.");


            // Listar todos os itens disponíveis (exemplo)
            System.out.println("Itens disponíveis:");
            inventoryController.getAllAvailableItems()
                    .forEach(item -> System.out.println(
                            item.getItemId()
                                    + " - " + item.getItemType()
                                    + " - " + item.getStockStatus()
                                    + " - " + item.getQuantity()
                    ));

            String venda = inventoryController.sellItemById("LIV-20240920T202824342004300");
            System.out.println("Status da venda: " + venda);

            // Listar todos os itens disponíveis (exemplo)
            System.out.println("Itens disponíveis:");
            inventoryController.generateReport(null, false)
                    .forEach(item -> System.out.println(
                            item.getItemId()
                                    + " - " + item.getItemType()
                                    + " - " + item.getStockStatus()
                                    + " - " + item.getQuantity()
                    ));

            // Listar doadores
            System.out.println("Doadores: ");
            donorService.getAllDonors().forEach(donor -> System.out.println(
                    donor.getId()
                            + " - " + donor.getName()
                            + " - " + donor.getEmail()
                            + " - " + donor.getDonationHistory()
            ));
            // Listar itens doados por um doador
            System.out.println("Itens doados por: " + newDonor1.getName());
            inventoryService.findItemsByDonorId(newDonor1.getId()).forEach(item -> System.out.println(
                    item.getItemId()
                            + " - " + item.getItemType()
                            + " - " + item.getDonor()
                            + " - " + item.getStockStatus()
                            + " - " + item.getQuantity()
            ));

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            DatabaseConnection.disconnect(connection); // Sempre feche a conexão
            System.out.println("Conexão fechada com sucesso");
        }
    }
}
