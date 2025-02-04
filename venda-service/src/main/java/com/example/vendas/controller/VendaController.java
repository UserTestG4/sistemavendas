package com.example.vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public Map<String, Object> realizarVenda(@RequestBody Map<String, Object> venda) {
        try {
            Integer clienteId = (Integer) venda.get("clienteId");
            Double total = Double.parseDouble(venda.get("total").toString()); // Garante que seja um número decimal
            List<Map<String, Object>> produtos = (List<Map<String, Object>>) venda.get("produtos");

            if (clienteId == null || produtos == null || produtos.isEmpty() || total == null) {
                return Map.of("erro", "Dados inválidos. Cliente, produtos ou total ausente.");
            }

            // Insere a venda no banco de dados
            String insertVendaSql = "INSERT INTO vendas (cliente_id, total) VALUES (?, ?)";
            jdbcTemplate.update(insertVendaSql, clienteId, total);

            // Obtém o ID da venda recém criada
            Integer vendaId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

            // Insere os produtos vendidos
            String insertProdutoSql = "INSERT INTO vendas_produtos (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
            for (Map<String, Object> produto : produtos) {
                Integer produtoId = (Integer) produto.get("id");
                Integer quantidade = (Integer) produto.get("quantidade");
                Double preco = Double.parseDouble(produto.get("preco").toString());

                if (produtoId != null && quantidade != null && quantidade > 0) {
                    jdbcTemplate.update(insertProdutoSql, vendaId, produtoId, quantidade, preco);
                }
            }

            return Map.of("mensagem", "Venda registrada com sucesso", "venda_id", vendaId);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("erro", "Erro ao registrar a venda");
        }
    }

    @GetMapping
    public List<Map<String, Object>> listarVendas() {
        String sql = "SELECT v.id, c.nome AS clienteNome, v.total, v.data_venda FROM vendas v LEFT JOIN clientes c ON v.cliente_id = c.id";
        List<Map<String, Object>> vendas = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> venda : vendas) {
            int vendaId = (int) venda.get("id");

            // Buscar produtos da venda
            String produtosSql = "SELECT p.nome, vp.quantidade, vp.preco_unitario AS preco " +
                    "FROM vendas_produtos vp " +
                    "JOIN produtos p ON vp.produto_id = p.id " +
                    "WHERE vp.venda_id = ?";
            List<Map<String, Object>> produtos = jdbcTemplate.queryForList(produtosSql, vendaId);

            // Adiciona os produtos dentro de cada venda
            venda.put("produtos", produtos);
        }

        return vendas;
    }

    @GetMapping("/{id}")
    public Map<String, Object> buscarVendaPorId(@PathVariable int id) {
        String sql = "SELECT v.id, c.nome AS clienteNome, v.total FROM vendas v LEFT JOIN clientes c ON v.cliente_id = c.id WHERE v.id = ?";
        return jdbcTemplate.queryForMap(sql, id);
    }
}
