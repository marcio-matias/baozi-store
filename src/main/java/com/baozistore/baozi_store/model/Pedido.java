package com.baozistore.baozi_store.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;
    @ManyToOne(optional = false) @JoinColumn(name = "produto_id", nullable = false)
    @JsonIgnore
    private Produto produto;
    @Column(nullable = false)
    private Integer quantidade;
    public Long getId() { return id; }
    public Long getClienteId() { return cliente.getId(); }
    public Long getProdutoId() { return produto.getId(); }
    public Integer getQuantidade() { return quantidade; }
    public void setCliente(Cliente value) { cliente = value; }
    public void setProduto(Produto value) { produto = value; }
    public void setQuantidade(Integer value) { quantidade = value; }
}
