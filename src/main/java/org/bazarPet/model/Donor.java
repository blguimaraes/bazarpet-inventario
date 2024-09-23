package org.bazarPet.model;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Donor {
    private final UUID id;
    private final String name;
    private String email;
    private String phone;
    private List<InventoryItem> donatedItems;

    // Contrutor para um novo doador (gera um UUID para o doador)
    public Donor(String name, String email, String phone, List<InventoryItem> donatedItems) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.donatedItems = donatedItems != null ? donatedItems : new ArrayList<>();
    }

    // Contrutor para buscas no banco de dados (mantém o id existente)
    public Donor(UUID id, String name, String email, String phone, List<InventoryItem> donatedItems) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.donatedItems = donatedItems != null ? donatedItems : new ArrayList<>();
    }

    // Checa o id
    public UUID getId() {
        return id;
    }

    // Checa o nome
    public String getName() {
        return this.name;
    }

    // Checa o email
    public String getEmail() {
        return this.email;
    }

    // Checa o telefone
    public String getPhone() {
        return phone;
    }

    public List<InventoryItem> getDonationHistory() {
        return new ArrayList<>(this.donatedItems); // Return a copy to avoid external modifications
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addDonatedItem(InventoryItem item) {
        this.donatedItems.add(item);  // Adiciona o item à lista de itens doados
    }
}
