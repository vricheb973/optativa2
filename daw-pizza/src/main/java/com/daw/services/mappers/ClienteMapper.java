package com.daw.services.mappers;

import com.daw.persistence.entities.Cliente;
import com.daw.persistence.entities.Direccion;
import com.daw.services.dto.ClienteDTO;
import com.daw.services.dto.DireccionDTO;

public class ClienteMapper {
	
	public static ClienteDTO toDto(Cliente cliente) {
		ClienteDTO dto = new ClienteDTO();

		dto.setId(cliente.getId());
		dto.setNombre(cliente.getNombre());
		dto.setTelefono(cliente.getTelefono());
		dto.setEmail(cliente.getEmail());

		for(Direccion dir : cliente.getDirecciones()) {
			if(dir.getActiva()) {
				DireccionDTO dirDTO = new DireccionDTO();
				dirDTO.setId(dir.getId());
				dirDTO.setCalle(dir.getCalle());
				dirDTO.setNumero(dir.getNumero());
				dirDTO.setPoblacion(dir.getPoblacion());
				dirDTO.setActiva(dir.getActiva());
				
				dto.setDireccion(dirDTO);
			}
		}
		
		return dto;
	}

}
