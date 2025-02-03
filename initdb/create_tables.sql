-- Cria uma tabela de exemplo no banco de dados 'produto_db'
CREATE TABLE produtos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  descricao TEXT,
  valor DECIMAL(10,2) NOT NULL
);
