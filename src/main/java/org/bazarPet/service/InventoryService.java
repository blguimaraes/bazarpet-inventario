package org.bazarPet.service;

import org.bazarPet.exception.ItemNotFoundException;
import org.bazarPet.model.InventoryItem;
import org.bazarPet.repository.InventoryItemRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InventoryService {
    private final InventoryItemRepository inventoryItemRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public void addItem(InventoryItem item) {
        inventoryItemRepository.create(item);
    }

    public InventoryItem getItemById(String itemId) throws ItemNotFoundException {
        return inventoryItemRepository.findById(itemId);
    }

    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findAll();
    }

    // Métdo para listar itens disponíveis
    public List<InventoryItem> listAvailableItems() {
        return inventoryItemRepository.findAll().stream()
                .filter(InventoryItem::checkAvailability)
                .collect(Collectors.toList());
    }

    // Métdo para gerar um relatório de inventário com base em opções de filtro
    public List<InventoryItem> generateInventoryReport(String itemType, boolean onlyAvailable) {
        return inventoryItemRepository.findAll().stream()
                .filter(item -> itemType == null || item.getItemType().equals(itemType))
                .filter(item -> !onlyAvailable || item.checkAvailability())
                .collect(Collectors.toList());
    }

    public void updateItem(InventoryItem item) throws ItemNotFoundException {
        inventoryItemRepository.update(item);
    }

    public void deleteItem(String itemId) throws ItemNotFoundException, SQLException {
        inventoryItemRepository.delete(itemId);
    }

    public List<InventoryItem> findItemsByDonorId(UUID donorId){
        return inventoryItemRepository.findItemsByDonorId(donorId);
    }
}
