package com.daw.services.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.daw.persistence.entities.ClienteEntity;
import com.daw.services.dtos.ClienteDTO;

@Component
public class ClienteMapper {

	public ClienteDTO toDto(ClienteEntity clienteEntity) {
		if(clienteEntity == null) {
			return null;
		}
		
		ClienteDTO dto = new ClienteDTO();
		dto.setId(clienteEntity.getId());
		dto.setNombre(clienteEntity.getNombre());
		dto.setEmail(clienteEntity.getEmail());
		dto.setDireccion(clienteEntity.getDireccion());
		dto.setTelefono(clienteEntity.getTelefono());

		return dto;
	}
	
	public ClienteEntity toEntity(ClienteDTO dto) {
		if(dto == null) {
			return null;
		}
		
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setId(dto.getId());
		clienteEntity.setNombre(dto.getNombre());
		clienteEntity.setEmail(dto.getEmail());
		clienteEntity.setDireccion(dto.getDireccion());
		clienteEntity.setTelefono(dto.getTelefono());

		return clienteEntity;
	}
	
	public List<ClienteDTO> toDtos(List<ClienteEntity> entities) {
		return entities.stream()
				.map(c -> this.toDto(c))
				.toList();
	}
	
	public List<ClienteEntity> toEntities(List<ClienteDTO> dtos) {
		return dtos.stream()
				.map(c -> this.toEntity(c))
				.toList();
	}
	
}
