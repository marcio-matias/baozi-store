package com.baozistore.baozi_store.controller;

import com.baozistore.baozi_store.model.Cliente;
import com.baozistore.baozi_store.repository.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repository;
    private final PedidoRepository pedidos;
    public ClienteController(ClienteRepository repository, PedidoRepository pedidos) { this.repository = repository; this.pedidos = pedidos; }
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente value) {
        if (value.getNome() == null || value.getNome().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obrigatório");
        value.setNome(value.getNome().trim());
        value.setId(null);
        if (value.getClienteDesde() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de cadastro obrigatória");
        Cliente saved = repository.saveAndFlush(value);
        return ResponseEntity.created(URI.create("/clientes/" + saved.getId())).body(saved);
    }
    @GetMapping
    public List<Cliente> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Cliente consultar(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Cliente value = consultar(id);
        if (pedidos.existsByCliente_Id(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Existem pedidos vinculados. Exclua os pedidos primeiro.");
        repository.delete(value);
        repository.flush();
        return ResponseEntity.noContent().build();
    }
}
