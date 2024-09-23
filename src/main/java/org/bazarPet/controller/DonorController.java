package org.bazarPet.controller;

import org.bazarPet.exception.DonorNotFoundException;
import org.bazarPet.model.Donor;
import org.bazarPet.service.DonorService;

import java.util.List;
import java.util.UUID;

public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    // Endpoint para adicionar um novo doador
    public void addDonor(Donor donor) {
        donorService.registerDonor(donor);
        System.out.println("Doador adicionado com sucesso.");
    }

    // Endpoint para buscar um doador pelo ID
    public Donor getDonorById(String donorId) {
        try {
            return donorService.getDonorById(donorId);
        } catch (DonorNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    // Endpoint para listar todos os doadores
    public List<Donor> getAllDonors() {
        return donorService.getAllDonors();
    }

    // Endpoint para atualizar um doador
    public void updateDonor(Donor donor) {
        try {
            donorService.updateDonor(donor);
            System.out.println("Doador atualizado com sucesso.");
        } catch (DonorNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint para deletar um doador
    public void deleteDonor(UUID donorId) {
        try {
            donorService.deleteDonor(donorId);
            System.out.println("Doador deletado com sucesso.");
        } catch (DonorNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
