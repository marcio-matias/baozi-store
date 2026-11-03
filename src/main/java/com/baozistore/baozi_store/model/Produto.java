package com.baozistore.baozi_store.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;
    @Column(nullable = false)
    private Boolean estoque;
    public Long getId() { return id; }
    public void setId(Long value) { this.id = value; }
    public String getNome() { return nome; }
    public void setNome(String value) { this.nome = value; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal value) { this.preco = value; }
    public Boolean getEstoque() { return estoque; }
    public void setEstoque(Boolean value) { this.estoque = value; }
}
