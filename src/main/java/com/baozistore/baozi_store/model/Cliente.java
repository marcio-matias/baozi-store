package com.baozistore.baozi_store.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private LocalDate clienteDesde;
    public Long getId() { return id; }
    public void setId(Long value) { this.id = value; }
    public String getNome() { return nome; }
    public void setNome(String value) { this.nome = value; }
    public LocalDate getClienteDesde() { return clienteDesde; }
    public void setClienteDesde(LocalDate value) { this.clienteDesde = value; }
}
