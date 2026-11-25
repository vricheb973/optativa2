package com.daw.services.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.daw.persistence.entities.PedidoEntity;
import com.daw.services.dtos.PedidoOutputDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoMapper {
	
	private final PizzaPedidoMapper pizzaPedidoMapper;

	//Método de salida (genera un Output)
	public PedidoOutputDTO toDto(PedidoEntity entity) {
		if(entity == null) {
			return null;
		}
		
		PedidoOutputDTO dto = new PedidoOutputDTO();
		dto.setId(entity.getId());
		dto.setClienteId(entity.getCliente().getId());
		dto.setNombreCliente(entity.getCliente().getNombre());
		dto.setTelefono(entity.getCliente().getTelefono());
		dto.setDireccion(entity.getCliente().getDireccion());
		dto.setFecha(entity.getFecha());
		dto.setTotal(entity.getTotal());
		dto.setMetodo(entity.getMetodo());
		dto.setNotas(entity.getNotas());
		dto.setPizzas(this.pizzaPedidoMapper.toDtos(entity.getPizzas()));

		return dto;
	}
	
//	//Método de entrada (genera un entity)
//	public PedidoEntity toEntity(PedidoInputDTO dto) {
//		if(dto == null) {
//			return null;
//		}
//		
//		ClienteEntity cliente = new ClienteEntity();
//		cliente.setId(dto.getClienteId());
//		
//		PedidoEntity entity = new PedidoEntity();
//		entity.setMetodo(dto.getMetodo());
//		entity.setNotas(dto.getNotas());
//		entity.setCliente(cliente);
//
//		return entity;
//	}
	
	public List<PedidoOutputDTO> toDtos(List<PedidoEntity> entities) {
		return entities.stream()
				.map(p -> this.toDto(p))
				.toList();
	}
	
//	public List<PedidoEntity> toEntities(List<PedidoInputDTO> dtos) {
//		return dtos.stream()
//				.map(p -> this.toEntity(p))
//				.toList();
//	}
//	
	

}
