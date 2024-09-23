package org.bazarPet.repository.jdbc;

import org.bazarPet.exception.DonorNotFoundException;
import org.bazarPet.exception.ItemNotFoundException;
import org.bazarPet.model.Donor;
import org.bazarPet.repository.DonorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DonorJdbcRepository implements DonorRepository {
    private final Connection connection;

    public DonorJdbcRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Donor donor) {
        String sql = "INSERT INTO Donor (id, name, email, phone) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, donor.getId().toString());
            statement.setString(2, donor.getName());
            statement.setString(3, donor.getEmail());
            statement.setString(4, donor.getPhone());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    @Override
    public Donor findByName(String name) throws DonorNotFoundException {
        // Implementar a lógica se necessário
        return null;
    }

    @Override
    public Donor findById(String donorId) throws DonorNotFoundException {
        String sql = "SELECT * FROM Donor WHERE id = ?";
        Donor donor = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, donorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                donor = mapResultSetToDonor(resultSet);
            } else {
                throw new DonorNotFoundException("Doador com ID " + donorId + " não foi encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }

        return donor;
    }

    @Override
    public List<Donor> findAll() {
        String sql = "SELECT * FROM Donor";
        List<Donor> donors = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Donor donor = mapResultSetToDonor(resultSet);
                donors.add(donor);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }

        return donors;
    }

    @Override
    public void update(Donor donor) throws DonorNotFoundException {
        String sql = "UPDATE Donor SET name = ?, email = ?, phone = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, donor.getName());
            statement.setString(2, donor.getEmail());
            statement.setString(3, donor.getPhone());
            statement.setString(4, donor.getId().toString()); // Usando UUID como chave primária

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DonorNotFoundException("Doador não foi encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    @Override
    public void delete(UUID donorId) throws DonorNotFoundException {
        String sql = "DELETE FROM Donor WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, donorId.toString());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DonorNotFoundException("Doador com ID " + donorId + " não foi encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Métdo auxiliar para mapear o ResultSet em um Donor
    private Donor mapResultSetToDonor(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String phone= resultSet.getString("phone");

        // Preservando o ID do banco de dados
        return new Donor(UUID.fromString(id), name, email, phone, null);
    }
}
