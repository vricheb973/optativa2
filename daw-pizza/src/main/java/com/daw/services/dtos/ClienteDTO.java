package com.daw.services.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO {
	
	private Long id;
	
	@NotBlank(message = "El nombre del cliente no puede estar vacío")
	@Size(max = 30, message = "El nombre no puede superar los 30 caracteres")
	private String nombre;

	@NotBlank(message = "La dirección no puede estar vacía")
	@Size(max = 100, message = "La dirección no puede superar los 100 caracteres")
	private String direccion;

	@NotBlank(message = "El email no puede estar vacío")
	@Email(message = "El formato del email no es válido")
	@Size(max = 50, message = "El email no puede superar los 50 caracteres")
	private String email;

	@NotBlank(message = "El teléfono no puede estar vacío")
	@Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
	private String telefono;

}
