package com.daw.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.PedidoEntity;
import com.daw.persistence.repositories.PedidoRepository;
import com.daw.services.exceptions.PedidoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {
	
	private final PedidoRepository pedidoRepository;
	
	public List<PedidoEntity> findAll(){
		return this.pedidoRepository.findAll();
	}
	
	public PedidoEntity findById(long idPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new PedidoNotFoundException("El ID indicado no existe. ");
		}

		return this.pedidoRepository.findById(idPedido).get();
	}
	
	public PedidoEntity create(PedidoEntity pedido) {		
		pedido.setId(0L);
		pedido.setFecha(LocalDateTime.now());
		pedido.setTotal(0.0);

		
		return this.pedidoRepository.save(pedido);
	}

	public PedidoEntity update(long idPedido, PedidoEntity pedidoEntity) {
		if(idPedido != pedidoEntity.getId()) {
			throw new PedidoNotFoundException("El ID del path y del body no coinciden. ");
		}
		
		PedidoEntity pedidoBD = this.findById(idPedido);
		pedidoBD.setIdCliente(pedidoEntity.getIdCliente());
		pedidoBD.setMetodo(pedidoEntity.getMetodo());
		pedidoBD.setNotas(pedidoEntity.getNotas());

		return this.pedidoRepository.save(pedidoBD);
	}

	public void deleteById(long idPedido) {
		if (!this.pedidoRepository.existsById(idPedido)) {
			throw new PedidoNotFoundException("El ID indicado no existe. ");
		}

		this.pedidoRepository.deleteById(idPedido);
	}

}
