package com.example.vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")  // Permite requisições de qualquer origem
@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public Map<String, Object> realizarVenda(@RequestBody Map<String, Object> venda) {
        String sql = "INSERT INTO vendas (cliente_id, produto_id, quantidade) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, venda.get("cliente_id"), venda.get("produto_id"), venda.get("quantidade"));
        return venda;
    }

    @GetMapping
    public List<Map<String, Object>> listarVendas() {
        String sql = "SELECT * FROM vendas";
        return jdbcTemplate.queryForList(sql);
    }
}
