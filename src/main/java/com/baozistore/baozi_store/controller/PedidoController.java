package com.baozistore.baozi_store.controller;

import com.baozistore.baozi_store.model.*;
import com.baozistore.baozi_store.repository.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    public record NovoPedido(Long clienteId, Long produtoId, Integer quantidade) {}
    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;
    public PedidoController(PedidoRepository pedidos, ClienteRepository clientes, ProdutoRepository produtos) {
        this.pedidos = pedidos; this.clientes = clientes; this.produtos = produtos;
    }
    @PostMapping @Transactional
    public ResponseEntity<Pedido> criar(@RequestBody NovoPedido input) {
        if (input.clienteId() == null || input.produtoId() == null || input.quantidade() == null || input.quantidade() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe clienteId, produtoId e quantidade positiva");
        Cliente cliente = clientes.findById(input.clienteId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        Produto produto = produtos.findById(input.produtoId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        if (!Boolean.TRUE.equals(produto.getEstoque())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto indisponível");
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente); pedido.setProduto(produto); pedido.setQuantidade(input.quantidade());
        Pedido saved = pedidos.saveAndFlush(pedido);
        return ResponseEntity.created(URI.create("/pedidos/" + saved.getId())).body(saved);
    }
    @GetMapping
    public List<Pedido> listar() { return pedidos.findAll(); }
    @GetMapping("/{id}")
    public Pedido consultar(@PathVariable Long id) { return pedidos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado")); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidos.delete(consultar(id)); pedidos.flush(); return ResponseEntity.noContent().build();
    }
}
