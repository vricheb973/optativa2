package com.daw.services.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.daw.persistence.entities.enums.MetodoPedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoOutputDTO {
	
	private Long id;
	private Long clienteId;
	private String nombreCliente;
	private String telefono;
	private String direccion;
	private LocalDateTime fecha;
	private Double total;
	private MetodoPedido metodo;
	private String notas;
	
	private List<PizzaPedidoOutputDTO> pizzas;

	public PedidoOutputDTO() {
		super();
		this.pizzas = new ArrayList<PizzaPedidoOutputDTO>();
	}
	
	
	
	

}
