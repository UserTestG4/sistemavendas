-- Cria a tabela vendas no banco de dados venda_db
CREATE TABLE vendas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cliente_id INT NOT NULL,
  produto_id INT NOT NULL,
  quantidade INT NOT NULL,
  data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
