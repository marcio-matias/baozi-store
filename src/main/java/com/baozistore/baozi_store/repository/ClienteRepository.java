package com.baozistore.baozi_store.repository;

import com.baozistore.baozi_store.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
