package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.repositories.PizzaPedidoRepository;
import com.daw.services.dtos.PizzaPedidoDto;
import com.daw.services.mappers.PizzaPedidoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaPedidoService {

	private final PizzaPedidoRepository pizzaPedidoRepository;
	
	public List<PizzaPedidoDto> findAll(){
		return  PizzaPedidoMapper.toDtos(this.pizzaPedidoRepository.findAll());
	}
	
	
}
