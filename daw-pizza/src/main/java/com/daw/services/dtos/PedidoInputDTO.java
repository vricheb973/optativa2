package com.daw.services.dtos;

import com.daw.persistence.entities.enums.MetodoPedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PedidoInputDTO {
	
	@NotNull(message = "El identificador del cliente (clienteId) es obligatorio")
	private Long clienteId;
	
	private MetodoPedido metodo;
	
	@Size(max = 200, message = "Las notas no pueden superar los 200 caracteres")
	private String notas;

}
