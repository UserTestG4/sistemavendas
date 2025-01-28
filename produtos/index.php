<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

$host = "mysql"; // Nome do serviço MySQL no Docker
$user = "root";
$pass = "password"; // Senha definida no docker-compose.yml
$dbname = "produtos_db"; // Nome do banco

$mysqli = new mysqli($host, $user, $pass, $dbname);

if ($mysqli->connect_error) {
    die(json_encode(["error" => "❌ Erro na conexão: " . $mysqli->connect_error]));
}

// **Adicionar Produto (POST)**
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $nome = $_POST['nome'] ?? '';
    $descricao = $_POST['descricao'] ?? '';
    $valor = $_POST['valor'] ?? 0;

    $query = "INSERT INTO produtos (nome, descricao, valor) VALUES (?, ?, ?)";
    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("ssd", $nome, $descricao, $valor);

    if ($stmt->execute()) {
        echo json_encode(["success" => true]);
    } else {
        echo json_encode(["success" => false, "error" => $stmt->error]);
    }
    $stmt->close();
}

// **Listar Produtos (GET)**
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query = "SELECT * FROM produtos";
    $result = $mysqli->query($query);

    $produtos = [];
    while ($row = $result->fetch_assoc()) {
        $produtos[] = $row;
    }

    echo json_encode($produtos);
}

$mysqli->close();
?>
