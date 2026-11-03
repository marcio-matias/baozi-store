package com.baozistore.baozi_store.controller;

import com.baozistore.baozi_store.model.Produto;
import com.baozistore.baozi_store.repository.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoRepository repository;
    private final PedidoRepository pedidos;
    public ProdutoController(ProdutoRepository repository, PedidoRepository pedidos) { this.repository = repository; this.pedidos = pedidos; }
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto value) {
        if (value.getNome() == null || value.getNome().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obrigatório");
        value.setNome(value.getNome().trim());
        value.setId(null);
        if (value.getPreco() == null || value.getPreco().signum() <= 0 || value.getPreco().stripTrailingZeros().scale() > 2 || value.getPreco().compareTo(new java.math.BigDecimal("9999999999.99")) > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preço deve ser positivo, com até duas casas decimais e no máximo 9999999999.99");
        if (value.getEstoque() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Disponibilidade obrigatória");
        Produto saved = repository.saveAndFlush(value);
        return ResponseEntity.created(URI.create("/produtos/" + saved.getId())).body(saved);
    }
    @GetMapping
    public List<Produto> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Produto consultar(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado")); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Produto value = consultar(id);
        if (pedidos.existsByProduto_Id(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Existem pedidos vinculados. Exclua os pedidos primeiro.");
        repository.delete(value);
        repository.flush();
        return ResponseEntity.noContent().build();
    }
}
