package com.baozistore.baozi_store.repository;

import com.baozistore.baozi_store.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsByCliente_Id(Long id);
    boolean existsByProduto_Id(Long id);
}
