package com.daw.services.mappers;

import java.util.List;

import com.daw.persistence.entities.PizzaPedidoEntity;
import com.daw.services.dtos.PizzaPedidoDto;

public class PizzaPedidoMapper {
	
	public static PizzaPedidoDto toDto(PizzaPedidoEntity pizzaPedidoEntity) {
		PizzaPedidoDto dto = new PizzaPedidoDto();
		dto.setNombreCliente(pizzaPedidoEntity.getPedido().getCliente().getNombre());
		dto.setPizza(pizzaPedidoEntity.getPizza().getNombre());
		dto.setCantidad(pizzaPedidoEntity.getCantidad());
		dto.setPrecio(pizzaPedidoEntity.getPrecio());
		
		return dto;
	}
	
	public static List<PizzaPedidoDto> toDtos(List<PizzaPedidoEntity> pizzaPedidoEntities){
		return pizzaPedidoEntities.stream().map(pp -> {
			PizzaPedidoDto dto = toDto(pp);
			return dto;
		})
		.toList();
	}

}
