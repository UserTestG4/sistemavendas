CREATE DATABASE IF NOT EXISTS produtos_db;
CREATE DATABASE IF NOT EXISTS vendas_db;

USE produtos_db;
CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    valor DECIMAL(10,2) NOT NULL
);

USE vendas_db;
CREATE TABLE IF NOT EXISTS vendas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT NOT NULL,
    cliente_id INT NOT NULL,
    quantidade INT NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO produtos (nome, descricao, valor) VALUES
('Produto A', 'Descrição do Produto A', 100.00),
('Produto B', 'Descrição do Produto B', 150.00);
