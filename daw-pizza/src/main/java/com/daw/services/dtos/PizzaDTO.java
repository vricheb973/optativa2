package com.daw.services.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PizzaDTO {

	private Long id;

	@NotBlank(message = "El nombre de la pizza no puede estar vacío")
	@Size(max = 30, message = "El nombre no puede superar los 30 caracteres")
	private String nombre;

	@Size(max = 150, message = "La descripción no puede superar los 150 caracteres")
	private String descripcion;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser superior a 0")
	private Double precio;
	
    private Boolean vegetariana;
    private Boolean vegana;
    private Boolean bordes;
    private Boolean disponible;

}
