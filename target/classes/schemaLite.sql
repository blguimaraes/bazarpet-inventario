-- schema.sql para SQLite

-- Ativar suporte a FOREIGN KEY no SQLite
PRAGMA foreign_keys = ON;

-- Criação da tabela Donor
CREATE TABLE Donor (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT,
    phone TEXT
);

-- Criação da tabela InventoryItem
CREATE TABLE InventoryItem (
    item_id TEXT PRIMARY KEY, -- Usando itemId como chave primária
    donor_id TEXT,
    item_type TEXT NOT NULL,
    price REAL NOT NULL, -- REAL é adequado para valores decimais
    stock_status TEXT NOT NULL,
    donation_date TEXT NOT NULL DEFAULT (DATETIME('now')), -- Define a data de doação como uma string ISO 8601
    quantity INTEGER NOT NULL,
    FOREIGN KEY (donor_id) REFERENCES Donor(id) ON DELETE SET NULL
);
