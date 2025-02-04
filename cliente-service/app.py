from flask import Flask, request, jsonify
from flask_cors import CORS  # Importa o flask-cors
import mysql.connector
import os

app = Flask(__name__)
CORS(app)  # Habilita o CORS para todas as rotas

# Configuração do MySQL
db_config = {
    'host': os.environ.get('MYSQL_HOST', 'mysql'),  # Nome correto do serviço do MySQL no docker-compose
    'user': os.environ.get('MYSQL_USER', 'myuser'),
    'password': os.environ.get('MYSQL_PASSWORD', 'myuserpassword'),
    'database': os.environ.get('MYSQL_DATABASE', 'sistema_vendas')  # Banco unificado
}


def get_db_connection():
    return mysql.connector.connect(**db_config)

@app.route('/clientes', methods=['POST'])
def cadastrar_cliente():
    data = request.get_json()
    nome = data.get('nome')
    telefone = data.get('telefone')
    
    conn = get_db_connection()
    cursor = conn.cursor()
    sql = "INSERT INTO clientes (nome, telefone) VALUES (%s, %s)"
    cursor.execute(sql, (nome, telefone))
    conn.commit()
    cliente_id = cursor.lastrowid
    cursor.close()
    conn.close()
    
    return jsonify({'id': cliente_id, 'nome': nome, 'telefone': telefone}), 201

@app.route('/clientes', methods=['GET'])
def listar_clientes():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM clientes")
    clientes = cursor.fetchall()
    cursor.close()
    conn.close()
    
    return jsonify(clientes)


@app.route('/clientes/<int:id>', methods=['GET'])
def buscar_cliente(id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM clientes WHERE id = %s", (id,))
    cliente = cursor.fetchone()
    cursor.close()
    conn.close()

    if cliente:
        return jsonify(cliente)
    else:
        return jsonify({"erro": "Cliente não encontrado"}), 404


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)