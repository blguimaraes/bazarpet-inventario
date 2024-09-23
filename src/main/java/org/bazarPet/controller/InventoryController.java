package org.bazarPet.controller;

import org.bazarPet.exception.ItemNotFoundException;
import org.bazarPet.model.InventoryItem;
import org.bazarPet.service.DonorService;
import org.bazarPet.service.InventoryService;

import java.sql.SQLException;
import java.util.List;

public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService, DonorService donorService) {
        this.inventoryService = inventoryService;
    }

    // Endpoint para adicionar um novo item
    public void addItem(InventoryItem inventoryItem) {
        inventoryService.addItem(inventoryItem);
    }

    // Endpoint para vender ou doar um item
    public String sellItemById(String itemId) {
        try {
            // Busca o item pelo itemId usando o serviço
            InventoryItem item = inventoryService.getItemById(itemId);

            // Realiza a venda e obtém a mensagem de resultado
            String result = item.sellItem(itemId);

            // Atualiza o item no banco de dados após a venda
            inventoryService.updateItem(item);

            return result;
        } catch (ItemNotFoundException e) {
            return "Item não encontrado: " + e.getMessage();
        }
    }

    // Endpoint para buscar um item pelo ID
    public InventoryItem getItemById(String itemId) {
        try {
            return inventoryService.getItemById(itemId);
        } catch (ItemNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    // Endpoint para listar todos os itens disponíveis
    public List<InventoryItem> getAllAvailableItems() {
        return inventoryService.listAvailableItems();
    }

    // Endpoint para atualizar um item existente
    public void updateItem(InventoryItem item) {
        try {
            inventoryService.updateItem(item);
            System.out.println("Item atualizado com sucesso.");
        } catch (ItemNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Endpoint para deletar um item pelo ID
    public void deleteItem(String itemId) {
        try {
            inventoryService.deleteItem(itemId);
            System.out.println("Item deletado com sucesso.");
        } catch (ItemNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint para gerar um relatório de inventário por tipo e disponibilidade
    public List<InventoryItem> generateReport(String itemType, boolean onlyAvailable) {
        return inventoryService.generateInventoryReport(itemType, onlyAvailable);
    }
}
