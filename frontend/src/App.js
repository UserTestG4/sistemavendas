import logo from './logo.svg';
import './App.css';

import React, { useEffect, useState } from "react";

function App() {
    const [produtos, setProdutos] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8081") // Endpoint do microserviço de produtos
            .then((response) => response.json())
            .then((data) => setProdutos(data));
    }, []);

    return (
        <div>
            <h1>Lista de Produtos</h1>
            <ul>
                {produtos.map((produto) => (
                    <li key={produto.id}>
                        {produto.nome} - R$ {produto.valor}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default App;
