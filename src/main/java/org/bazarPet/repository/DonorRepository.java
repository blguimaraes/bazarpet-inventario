package org.bazarPet.repository;

import org.bazarPet.exception.DonorNotFoundException;
import org.bazarPet.model.Donor;

import java.util.List;
import java.util.UUID;

public interface DonorRepository {

    // Cria um novo doador
    void create(Donor donor);

    // Retorna um doador pelo nome (ou outro identificador)
    Donor findByName(String name) throws DonorNotFoundException;

    Donor findById(String donorId) throws DonorNotFoundException;

    // Retorna todos os doadores
    List<Donor> findAll();

    // Atualiza um doador existente
    void update(Donor donor) throws DonorNotFoundException;

    // Remove um doador pelo donorId
    void delete(UUID donorId) throws DonorNotFoundException;
}
