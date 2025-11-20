package com.daw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Pedido;
import com.daw.persistence.repositories.PedidoRepository;
import com.daw.services.dto.PedidoDTO;
import com.daw.services.exceptions.ClienteNotFoundException;
import com.daw.services.mappers.PedidoMapper;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;	
	
	public List<PedidoDTO> findAll(){
		return PedidoMapper.toDTOsFuncional(this.pedidoRepository.findAll());
	}
	
	public PedidoDTO findById(int idPedido) {
		if(!this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}
		
		return PedidoMapper.toDTO(this.pedidoRepository.findById(idPedido).get());
	}
	
	public Pedido create(Pedido pedido) {
		pedido.setId(0);
		
		return this.pedidoRepository.save(pedido);
	}
	
//	public Pedido update(int idPedido, Pedido pedido) {
//		Pedido pedidoBD = this.findById(idPedido);
//		pedidoBD.setIdCliente(pedido.getIdCliente());
//		pedidoBD.setFecha(pedido.getFecha());
//		pedidoBD.setTotal(pedido.getTotal());
//		pedidoBD.setMetodo(pedido.getMetodo());
//		pedidoBD.setNotas(pedido.getNotas());
//		
//		return this.pedidoRepository.save(pedido);
//	}
	
	public void deleteById(int idPedido) {
		if(!this.pedidoRepository.existsById(idPedido)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}
		
		this.pedidoRepository.deleteById(idPedido);
	}
	
}
