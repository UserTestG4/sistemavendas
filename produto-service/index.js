// produto-service/index.js
const express = require('express');
const mysql = require('mysql');
const bodyParser = require('body-parser');
const cors = require('cors');  // Importa o módulo CORS

const app = express();

// Configura middlewares
app.use(bodyParser.json());
app.use(cors()); // Habilita CORS para todas as origens

// Configuração do MySQL utilizando variáveis de ambiente
const connection = mysql.createConnection({
  host: process.env.MYSQL_HOST || 'mysql',
  user: process.env.MYSQL_USER || 'myuser',  // Agora está correto
  password: process.env.MYSQL_PASSWORD || 'myuserpassword',
  database: process.env.MYSQL_DATABASE || 'sistema_vendas'
});


// Tenta conectar ao MySQL
connection.connect(err => {
  if (err) {
    console.error('Erro ao conectar no MySQL:', err);
    process.exit(1); // Encerra a aplicação se não conseguir conectar
  }
  console.log('Conectado ao MySQL (Produto Service)');
});

// Endpoint para cadastro de produto
app.post('/produtos', (req, res) => {
  const { nome, descricao, valor } = req.body;
  const sql = 'INSERT INTO produtos (nome, descricao, valor) VALUES (?, ?, ?)';
  connection.query(sql, [nome, descricao, valor], (err, result) => {
    if (err) {
      console.error('Erro ao inserir produto:', err);
      return res.status(500).json({ error: err });
    }
    res.json({ id: result.insertId, nome, descricao, valor });
  });
});

// Endpoint para listar produtos
app.get('/produtos', (req, res) => {
  connection.query('SELECT * FROM produtos', (err, results) => {
    if (err) {
      console.error('Erro ao listar produtos:', err);
      return res.status(500).json({ error: err });
    }
    res.json(results);
  });
});

// Endpoint para buscar produto por ID
app.get('/produtos/:id', (req, res) => {
  const produtoId = req.params.id;
  const sql = 'SELECT * FROM produtos WHERE id = ?';

  connection.query(sql, [produtoId], (err, result) => {
    if (err) {
      console.error('Erro ao buscar produto:', err);
      return res.status(500).json({ error: err });
    }

    if (result.length === 0) {
      return res.status(404).json({ error: 'Produto não encontrado' });
    }

    res.json(result[0]);
  });
});


const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Produto Service rodando na porta ${PORT}`);
});