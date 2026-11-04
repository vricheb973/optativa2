package com.daw.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistence.entities.PizzaPedidoEntity;

public interface PizzaPedidoRepository extends JpaRepository<PizzaPedidoEntity, Long> {

}
