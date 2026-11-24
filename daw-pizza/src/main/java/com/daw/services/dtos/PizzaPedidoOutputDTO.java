package com.daw.services.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PizzaPedidoOutputDTO {
	
	private Long id;
	private Long pizzaId;
	private Long pedidoId;
	private String nombre;
	private Double cantidad;
	private Double precioUnitario;
	private Double precioTotal;

}
