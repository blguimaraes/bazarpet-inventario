package org.bazarPet.service;

import org.bazarPet.exception.DonorNotFoundException;
import org.bazarPet.model.Donor;
import org.bazarPet.repository.DonorRepository;

import java.util.List;
import java.util.UUID;

public class DonorService {
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public void registerDonor(Donor donor) {
        donorRepository.create(donor);
    }

    public Donor getDonorById(String donorId) throws DonorNotFoundException {
        try {
            return donorRepository.findById(donorId);
        } catch (DonorNotFoundException e) {
            System.out.println("Doador não encontrado");
            throw new DonorNotFoundException("Doador com ID " + donorId + " não foi encontrado.", e);
        }
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public void updateDonor(Donor donor) throws  DonorNotFoundException {
        donorRepository.update(donor);
    }

    public void deleteDonor(UUID donorId) throws DonorNotFoundException {
        donorRepository.delete(donorId);
    }
}
