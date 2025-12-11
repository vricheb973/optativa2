package com.daw.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DireccionDTO {
	
	private Integer id;
	private String calle;
	private String numero;
	private String poblacion;
	private Boolean activa;

}
