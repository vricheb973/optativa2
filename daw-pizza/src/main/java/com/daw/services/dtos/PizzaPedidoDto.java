package com.daw.services.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PizzaPedidoDto {
	
	private String nombreCliente;
	private String pizza;
	private double cantidad;
	private double precio;

}
