package com.daw.services.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PizzaPedidoInputDTO {
	
	@NotNull(message = "El identificador de la pizza (pizzaId) es obligatorio")
	private Long pizzaId;
	
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.5", inclusive = true, message = "La cantidad mínima debe ser 0.5")
	private Double cantidad;

}
