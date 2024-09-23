package org.bazarPet.repository;

import org.bazarPet.exception.ItemNotFoundException;
import org.bazarPet.model.InventoryItem;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface InventoryItemRepository {

    // Cria um novo item no inventário
    void create(InventoryItem item);

    // Retorna um item pelo ID
    InventoryItem findById(String itemId) throws ItemNotFoundException;

    // Retorna todos os itens no inventário
    List<InventoryItem> findAll();

    // Atualiza um item existente
    void update(InventoryItem item) throws ItemNotFoundException;

    // Remove um item pelo ID
    void delete(String itemId) throws ItemNotFoundException, SQLException;

    // Busca itens por doador
    List<InventoryItem> findItemsByDonorId(UUID donorId);
}
