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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
    private static final String[] ITEM_TYPES = {
            "Camisa", "Calça", "Calçado", "Acessório", "Livro"
    };

    private static final String[] DONORS_NAMES = {
            "Doador 1", "Doador 2", "Doador 3"
    };

    private static final Random random = new Random();

    private static List<Donor> createDonors() {
        List<Donor> donors = new ArrayList<>();

        donors.add(new Donor("João Silva", "joao@example.com", "123456789", null));
        donors.add(new Donor("Maria Oliveira", "maria@example.com", "987654321", null));
        donors.add(new Donor("Carlos Pereira", "carlos@example.com", "456789123", null));

        return donors;
    }

    private static List<InventoryItem> generateRandomItems(List<Donor> donors, int count) {
        List<InventoryItem> items = new ArrayList<>();
        String[] itemTypes = {"Camisa", "Calça", "Calçado", "Acessório", "Livro"};
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String itemType = itemTypes[random.nextInt(itemTypes.length)];
            float price = 10 + random.nextFloat() * 90;
            int quantity = 1 + random.nextInt(5);
            LocalDateTime donationDate = LocalDateTime.now().minusDays(random.nextInt(30));
            Donor donor = donors.get(random.nextInt(donors.size()));

            InventoryItem item = new InventoryItem(itemType, price, quantity, donor);
            item.setDonationDate(donationDate);
            items.add(item);
        }

        return items;
    }


    public static void main(String[] args) {
        List<Donor> donors = createDonors();
        List<InventoryItem> items = generateRandomItems(donors, 50);
        Connection connection = null;

        // Conexão com o banco de dados
        try {
            connection = DatabaseConnection.connect();
            System.out.println("Checando a existência do banco de dados e tabel:");
            DatabaseInitializer.initializeDatabase(connection);

            // Instanciar o repositório e serviço do doador (JDBC)
            DonorJdbcRepository donorRepository = new DonorJdbcRepository(connection);
            DonorService donorService = new DonorService(donorRepository);

            // Instanciar o repositório e serviço do inventário (JDBC)
            InventoryItemJdbcRepository inventoryRepository = new InventoryItemJdbcRepository(connection, donorRepository);
            InventoryService inventoryService = new InventoryService(inventoryRepository);

            // Instanciar o controlador passando o serviço
            DonorController donorController = new DonorController(donorService);
            InventoryController inventoryController = new InventoryController(inventoryService, donorService);

            // Inserir doadores no banco de dados
            for (Donor donor : donors) {
                donorRepository.create(donor);
            }

            // Inserir itens no banco de dados
            for (InventoryItem item : items) {
                inventoryController.addItem(item);
            }

            // Imprimir os itens inseridos
            items.forEach(item -> {
                System.out.println("Item: " + item.getItemType() + ", Doador: " + item.getDonorId());
            });

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
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        } finally {
            DatabaseConnection.disconnect(connection); // Sempre feche a conexão
            System.out.println("Conexão fechada com sucesso");
        }
    }
}
