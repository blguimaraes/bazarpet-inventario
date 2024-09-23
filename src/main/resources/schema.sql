-- schema.sql para MySQL

-- Criação da tabela Donor
CREATE TABLE Donor (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(15)
);

-- Criação da tabela InventoryItem
CREATE TABLE InventoryItem (
    item_id VARCHAR(50) PRIMARY KEY,
    donor_id VARCHAR(50),
    item_type VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_status VARCHAR(50) NOT NULL,
    donation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INT NOT NULL,
    FOREIGN KEY (donor_id) REFERENCES Donor(id) ON DELETE SET NULL
);
