package org.bazarPet.repository.jdbc;

import org.bazarPet.exception.DonorNotFoundException;
import org.bazarPet.exception.ItemNotFoundException;
import org.bazarPet.model.Donor;
import org.bazarPet.model.InventoryItem;
import org.bazarPet.repository.DonorRepository;
import org.bazarPet.repository.InventoryItemRepository;
import org.bazarPet.service.DonorService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryItemJdbcRepository implements InventoryItemRepository {
    private final Connection connection;
    private final DonorRepository donorRepository;


    public InventoryItemJdbcRepository(Connection connection, DonorRepository donorRepository) {
        this.connection = connection;
        this.donorRepository = donorRepository;
    }

    @Override
    public void create(InventoryItem item) {
        String sql = "INSERT INTO InventoryItem (item_id, item_type, price, stock_status, donation_date, quantity, donor_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getItemId());
            statement.setString(2, item.getItemType());
            statement.setFloat(3, item.getPrice());
            statement.setString(4, item.getStockStatus());
            statement.setString(5, item.getFormattedDonationDate());
            statement.setInt(6, item.getQuantity());
            statement.setString(7, item.getDonorId() != null ? item.getDonorId().toString() : null);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    @Override
    public InventoryItem findById(String itemId) throws ItemNotFoundException {
        String sql = "SELECT * FROM InventoryItem WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToInventoryItem(resultSet);
                } else {
                    throw new ItemNotFoundException("Item with ID " + itemId + " not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            throw new ItemNotFoundException("Error accessing the database");
        }
    }

    @Override
    public List<InventoryItem> findAll() {
        String sql = "SELECT * FROM InventoryItem";
        List<InventoryItem> items = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                items.add(mapResultSetToInventoryItem(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            return new ArrayList<>();
        }

        return items;
    }

    @Override
    public void update(InventoryItem item) throws ItemNotFoundException {
        String sql = "UPDATE InventoryItem SET item_type = ?, price = ?, stock_status = ?, quantity = ?, donor_id = ? WHERE item_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getItemType());
            statement.setFloat(2, item.getPrice());
            statement.setString(3, item.getStockStatus());
            statement.setInt(4, item.getQuantity());
            statement.setString(5, item.getDonorId() != null ? item.getDonorId().toString() : null);
            statement.setString(6, item.getItemId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new ItemNotFoundException("Item with ID " + item.getItemId() + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    @Override
    public void delete(String itemId) throws ItemNotFoundException {
        String sql = "DELETE FROM InventoryItem WHERE item_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, itemId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new ItemNotFoundException("Item with ID " + itemId + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private InventoryItem mapResultSetToInventoryItem(ResultSet resultSet) throws SQLException {
        String itemId = resultSet.getString("item_id");
        String itemType = resultSet.getString("item_type");
        float price = resultSet.getFloat("price");
        String stockStatus = resultSet.getString("stock_status");
        int quantity = resultSet.getInt("quantity");

        String donationDateStr = resultSet.getString("donation_date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime donationDate = LocalDateTime.parse(donationDateStr, formatter);

        String donorId = resultSet.getString("donor_id");

        Donor donor = null;
        if (donorId != null) {
            try {
                donor = donorRepository.findById(donorId);
            } catch (DonorNotFoundException e) {
                System.out.println("Doador com ID " + donorId + " não foi encontrado.");
            }
        }

        InventoryItem item = new InventoryItem(itemId, itemType, price, stockStatus, quantity, donor);
        item.setDonationDate(donationDate);


        return item;
    }

    // Busca itens por doador
    @Override
    public List<InventoryItem> findItemsByDonorId(UUID donorId) {
        String sql = "SELECT * FROM InventoryItem WHERE donor_id = ?";
        List<InventoryItem> items = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, donorId.toString());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                InventoryItem item = mapResultSetToInventoryItem(resultSet);
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return items;
    }
}
