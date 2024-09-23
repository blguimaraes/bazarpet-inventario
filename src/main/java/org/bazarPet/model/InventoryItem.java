package org.bazarPet.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.bazarPet.exception.ItemNotFoundException;

public class InventoryItem {
    private final String itemId;
    private final String itemType;
    private float price;
    private StockStatus stockStatus;
    private UUID donorId;
    private Donor donor;
    private int quantity;
    private LocalDateTime donationDate; // Usando LocalDateTime para data e hora

    // Formato ISO 8601 para Data e Horário
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Construtor para novos itens (gera o itemId customizado)
    public InventoryItem(String itemType, float price, int quantity, Donor donor) {
        this.itemId = itemType
                .substring(0, 3)
                .toUpperCase() + "-" + LocalDateTime.now()
                .toString().replace(":", "")
                .replace("-", "")
                .replace(".", "");
        this.itemType = itemType;
        this.price = price;
        this.stockStatus = StockStatus.EM_ESTOQUE;
        this.quantity = quantity;
        this.donor = donor;
        this.donorId = donor != null ? donor.getId() : new Donor("Anônimo", "", "", null).getId();
        this.donationDate = LocalDateTime.now(); // Atualiza a data da doação

        // Adiciona este item ao histórico de doações do doador
        if (donor != null) {
            donor.addDonatedItem(this);
        }
    }

    // Construtor para itens vindos do banco de dados usando sobrecarga de construtores (mantém o itemId existente)
    public InventoryItem(String itemId, String itemType, float price, String stockStatus, int quantity, Donor donor) {
        this.itemId = itemId;  // Usa o itemId recuperado do banco de dados
        this.itemType = itemType;
        this.price = price;
        this.stockStatus = StockStatus.valueOf(stockStatus); // Converte para enum
        this.quantity = quantity;
        this.donorId = donor != null ? donor.getId() : new Donor("Anônimo", "", "", null).getId();
        this.donationDate = LocalDateTime.now(); // ou um valor padrão

        // Adiciona este item ao histórico de doações do doador
        if (donor != null) {
            donor.addDonatedItem(this);
        }
    }

    // Checa o id do item
    public String getItemId() {
        return itemId;
    }

    // Checa a disponibilidade
    public boolean checkAvailability() {
        return quantity > 0;
    }

    // Checa a data e hora da doação
    public LocalDateTime getDonationDate() {
        return this.donationDate;
    }

    // Converte LocalDateTime para String (ISO 8601)
    public String getFormattedDonationDate() {
        return this.donationDate.format(FORMATTER);
    }

    // Define donationDate a partir de uma string (ISO 8601)
    public void setDonationDateFromString(String donationDateStr) {
        this.donationDate = LocalDateTime.parse(donationDateStr, FORMATTER);
    }

    // Checa o status do estoque
    public String getStockStatus() {
        if (stockStatus == StockStatus.EM_ESTOQUE) { return "EM_ESTOQUE"; }
        if (stockStatus == StockStatus.ESGOTADO) { return "ESGOTADO"; }
        else { return null; }
    }

    // Checa a quantidade disponível
    public int getQuantity() { return quantity; }

    // Checa o tipo do item
    public String getItemType() {
        return itemType;
    }

    // Checa o preço
    public float getPrice() {
        return price;
    }

    // Checa id do doador do item
    public UUID getDonorId() {
        return donorId;
    }

    // Checa o nome do doador do item
    public Donor getDonor() {
        return donor;
    }

    // Atualiza o preço
    public void updatePrice(float newPrice) {
        this.price = newPrice;
    }

    // Atualiza a quantidade
    public void updateQuantity(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantity = newQuantity;
        }
    }

    // Atualiza o status no estoque
    public void setStockStatus(String stockStatus) {
        if (stockStatus.equals("EM_ESTOQUE")) { this.stockStatus = StockStatus.EM_ESTOQUE; }
        if (stockStatus.equals("ESGOTADO")) { this.stockStatus =  StockStatus.ESGOTADO; }
    }

    // Ao vender ou doar um item, atualiza o status e o estoque
    public String sellItem(String itemId) {
        // Verifica se o itemId corresponde
        if (!this.itemId.equals(itemId)) {
            return "Item não encontrado.";
        }

        // Verifica se o item está fora de estoque
        if (this.stockStatus == StockStatus.ESGOTADO || this.quantity == 0) {
            return "Item fora de estoque.";
        }

        // Reduz a quantidade e atualiza o status, se necessário
        this.quantity -= 1;
        if (this.quantity == 0) {
            this.stockStatus = StockStatus.ESGOTADO;
            return "Item esgotado após a venda.";
        }

        return "Item vendido com sucesso.";
    }

    // Atualiza a data de doação
    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }

    // Atualiza o doador
    public void setDonor(Donor donor) {
        this.donorId = donor.getId();
    }
}
