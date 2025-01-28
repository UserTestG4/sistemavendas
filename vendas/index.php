<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

// Conectar ao banco
$host = "mysql"; // Nome do serviço MySQL no Docker
$user = "root";
$pass = "password"; // Senha definida no docker-compose.yml
$dbname = "vendas_db"; // Nome do banco

$mysqli = new mysqli($host, $user, $pass, $dbname);

if ($mysqli->connect_error) {
    die(json_encode(["error" => "❌ Erro na conexão: " . $mysqli->connect_error]));
}

// **Adicionar Venda (POST)**
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $produto_id = $_POST['produto_id'] ?? 0;
    $cliente_id = $_POST['cliente_id'] ?? 0;
    $quantidade = $_POST['quantidade'] ?? 0;
    $valor_total = $_POST['valor_total'] ?? 0;

    $query = "INSERT INTO vendas (produto_id, cliente_id, quantidade, valor_total) VALUES (?, ?, ?, ?)";
    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("iiid", $produto_id, $cliente_id, $quantidade, $valor_total);

    if ($stmt->execute()) {
        echo json_encode(["success" => true]);
    } else {
        echo json_encode(["success" => false, "error" => $stmt->error]);
    }
    $stmt->close();
}

// **Listar Vendas (GET)**
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query = "SELECT * FROM vendas";
    $result = $mysqli->query($query);

    $vendas = [];
    while ($row = $result->fetch_assoc()) {
        $vendas[] = $row;
    }

    echo json_encode($vendas);
}

$mysqli->close();
?>
