package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.repositories.PizzaPedidoRepository;
import com.daw.services.exceptions.PizzaNotFoundException;
import com.daw.services.exceptions.PizzaPedidoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaPedidoService {

	private final PizzaPedidoRepository pizzaPedidoRepository;
	
	public List<PizzaPedidoDto> findAll(){
		return  PizzaPedidoMapper.toDtos(this.pizzaPedidoRepository.findAll());
	}
	
	public PizzaPedidoEntity findById(long idPizzaPedido) {
		if(!this.pizzaPedidoRepository.existsById(idPizzaPedido)) {
			throw new PizzaPedidoNotFoundException("El ID indicado no existe. ");
		}
		
		return this.pizzaPedidoRepository.findById(idPizzaPedido).get();
	}
	
	public PizzaPedidoEntity create(PizzaPedidoEntity pizzaPedido) {
		pizzaPedido.setId(0);
		
		return this.pizzaPedidoRepository.save(pizzaPedido);
	}
	
	public PizzaPedidoEntity update(int idPizzaPedido, PizzaPedidoEntity pizzaPedido) {
		if(idPizzaPedido != pizzaPedido.getId()) {
			throw new PizzaPedidoNotFoundException("El ID del path y del body no coinciden. ");
		}
		
		PizzaPedidoEntity pizzaPedidoBD = this.findById(idPizzaPedido);
		pizzaPedidoBD.setIdPedido(pizzaPedido.getIdPedido());
		pizzaPedidoBD.setIdPizza(pizzaPedido.getIdPizza());
		pizzaPedidoBD.setPrecio(pizzaPedido.getPrecio());
		pizzaPedidoBD.setCantidad(pizzaPedido.getCantidad());
		
		return this.pizzaPedidoRepository.save(pizzaPedidoBD);
	}
	
	public void deleteById(long idPizzaPedido) {
		if(!this.pizzaPedidoRepository.existsById(idPizzaPedido)) {
			throw new PizzaNotFoundException("El ID indicado no existe. ");
		}
		
		this.pizzaPedidoRepository.deleteById(idPizzaPedido);
	}
	
}
