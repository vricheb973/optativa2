package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.PedidoEntity;
import com.daw.persistence.repositories.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {
	
	private final PedidoRepository pedidoRepository;
	
	public List<PedidoEntity> findAll(){
		return this.pedidoRepository.findAll();
	}

}
