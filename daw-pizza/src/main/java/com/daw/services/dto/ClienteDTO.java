package com.daw.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO {
	
	private int id;
	private String nombre;
	private String telefono;
	private String email;
	private DireccionDTO direccion;
	
	

}
