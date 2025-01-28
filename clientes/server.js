const express = require("express");
const mongoose = require("mongoose");

const app = express();
app.use(express.json());

mongoose.connect("mongodb://mongo:27017/clientes_db", { useNewUrlParser: true, useUnifiedTopology: true });

const Cliente = mongoose.model("Cliente", new mongoose.Schema({
    nome: String,
    telefone: String
}));

app.post("/clientes", async (req, res) => {
    const cliente = new Cliente(req.body);
    await cliente.save();
    res.send(cliente);
});

app.get("/clientes", async (req, res) => {
    const clientes = await Cliente.find();
    res.send(clientes);
});

app.listen(8082, () => console.log("Microserviço de Clientes rodando na porta 8082"));
