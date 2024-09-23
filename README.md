# bazarpet-inventario
Protótipo em Java do sistema de inventário para a projeto de proteção animal BazarPet JF

# Sistema de Inventário e Doações

Este sistema é responsável por gerenciar o inventário de itens doados e seus doadores, permitindo a consulta, atualização e exclusão de dados. A solução integra a parte de backend para suportar uma futura implementação de front-end.

## Estrutura do Sistema

O sistema é composto pelas seguintes classes principais:

### Donor (Doador)
Representa os doadores que contribuem com itens para o inventário.

#### Atributos:
- `donorId`: Identificador único do doador.
- `name`: Nome do doador.
- `email`: Endereço de email do doador.
- `phone`: Telefone de contato do doador.

#### Métodos:
- `getId()`: Retorna o ID do doador.
- `getName()`: Retorna o nome do doador.
- `getEmail()`: Retorna o email do doador.
- `getPhone()`: Retorna o telefone do doador.
- `getDonationHistory()`: Retorna o histórico de doações do doador.
- `setEmail(String email)`: Atualiza o email do doador.
- `setPhone(String phone)`: Atualiza o telefone do doador.

### InventoryItem (Item de Inventário)
Representa um item que foi doado e está sendo gerenciado no inventário.

#### Atributos:
- `itemId`: Identificador único do item.
- `itemType`: Tipo ou categoria do item.
- `price`: Preço atribuído ao item.
- `stockStatus`: Status de estoque do item (`EM_ESTOQUE` ou `ESGOTADO`).
- `donorId`: ID do doador associado ao item.
- `donor`: Objeto `Donor` associado ao item.
- `quantity`: Quantidade do item.
- `donationDate`: Data em que o item foi doado.

#### Métodos:
- `getItemId()`: Retorna o ID do item.
- `checkAvailability()`: Verifica se o item está disponível no estoque.
- `getDonationDate()`: Retorna a data de doação do item.
- `getStockStatus()`: Retorna o status de estoque do item.
- `getQuantity()`: Retorna a quantidade disponível do item.
- `getItemType()`: Retorna o tipo do item.
- `getPrice()`: Retorna o preço do item.
- `getDonorId()`: Retorna o ID do doador.
- `updatePrice(float newPrice)`: Atualiza o preço do item.
- `updateQuantity(int newQuantity)`: Atualiza a quantidade do item.
- `setStockStatus(String stockStatus)`: Define o status de estoque do item.
- `sellItem(String itemId)`: Marca o item como vendido.
- `setDonor(Donor donor)`: Define o doador associado ao item.
- `setDonationDate(LocalDateTime donationDate)`: Define a data de doação do item.

### StockStatus (Status de Estoque)
Enumeração que define os possíveis status de estoque dos itens:
- `EM_ESTOQUE`: O item está disponível no estoque.
- `ESGOTADO`: O item está esgotado.

### Repositórios

### DatabaseConnection
Gerencia as operações de conexão com o banco de dados.
Configurado para SQLite e MySQL, com SQLite no momento.

#### Métodos
-`connect`: Obtém uma conexão com o banco de dados.
-`disconnect`: Fecha a conexão com o banco de dados.

#### InventoryItemRepository (interface) e InventoryItemJdbcRepository (implementação)
Gerencia as operações de persistência de `InventoryItem`.

##### Métodos:
- `create(InventoryItem item)`: Cria um novo item de inventário.
- `findById(String itemId)`: Busca um item pelo seu ID.
- `findAll()`: Retorna todos os itens de inventário.
- `update(InventoryItem item)`: Atualiza um item de inventário.
- `delete(String itemId)`: Exclui um item de inventário.
- `findByDonorId(String donorId)`: Busca todos os itens de um doador específico.

#### DonorRepository (interface) e DonorJdbcRepository (implementação)
Gerencia as operações de persistência de `Donor`.

##### Métodos:
- `create(Donor donor)`: Cria um novo doador.
- `findById(String donorId)`: Busca um doador pelo seu ID.
- `findByName(String name)`: Busca doadores pelo nome.
- `findAll()`: Retorna todos os doadores.
- `update(Donor donor)`: Atualiza os dados de um doador.
- `delete(String donorId)`: Exclui um doador.

### Serviços

#### InventoryService
Camada de serviço para gerenciar operações relacionadas a `InventoryItem`.

##### Métodos:
- `addItem(InventoryItem item)`: Adiciona um novo item ao inventário.
- `getItemById(String itemId)`: Busca um item pelo ID.
- `sellItem(String itemId)`: Marca um item como vendido.
- `getAllItems()`: Retorna todos os itens de inventário.
- `getAllAvailableItems()`: Retorna apenas os itens disponíveis em estoque.
- `updateItem(InventoryItem item)`: Atualiza os detalhes de um item de inventário.
- `deleteItem(String itemId)`: Remove um item do inventário.
- `findItemsByDonorId(String donorId)`: Retorna os itens de inventário associados a um doador.

#### DonorService
Camada de serviço para gerenciar operações relacionadas a `Donor`.

##### Métodos:
- `registerDonor(Donor donor)`: Registra um novo doador.
- `getDonorById(String donorId)`: Busca um doador pelo ID.
- `getAllDonors()`: Retorna todos os doadores.
- `updateDonor(Donor donor)`: Atualiza os dados de um doador.
- `deleteDonor(String donorId)`: Remove um doador.

## Relacionamentos
- Um `Donor` pode doar múltiplos `InventoryItem`s.
- `InventoryItemRepository` e `DonorRepository` gerenciam, respectivamente, as entidades `InventoryItem` e `Donor`.
- Os serviços `InventoryService` e `DonorService` encapsulam as operações dos repositórios e lidam com a lógica de negócios.

## Como Usar

1. Clone o repositório.
2. Configure a conexão com o banco de dados no arquivo `application.properties`.
3. Utilize os serviços de `InventoryService` e `DonorService` para gerenciar os dados de inventário e doadores.
4. App, disponível em src.main.java.org.bazarPet, possui um exemplo de fluxo básico.

## Futuras Melhorias
- Permitir a adição de itens sem um doador específicado.
- Tornar os endpoints compatíveis com operações REST.
- Implementação de uma interface gráfica para o sistema.
- Sistema de relatórios com visualização gráfica dos dados de doações e inventário.
