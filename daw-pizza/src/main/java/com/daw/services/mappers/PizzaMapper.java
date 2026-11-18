package com.daw.services.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.daw.persistence.entities.PizzaEntity;
import com.daw.services.dtos.PizzaDTO;

@Component
public class PizzaMapper {

    public PizzaDTO toDto(PizzaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        PizzaDTO dto = new PizzaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecio(entity.getPrecio());
        dto.setVegetariana(entity.getVegetariana());
        dto.setVegana(entity.getVegana());
        dto.setBordes(entity.getBordes());
        dto.setDisponible(entity.getDisponible());
        
        return dto;
    }

    public PizzaEntity toEntity(PizzaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        PizzaEntity entity = new PizzaEntity();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPrecio(dto.getPrecio());
        entity.setVegetariana(dto.getVegetariana());
        entity.setVegana(dto.getVegana());
        entity.setBordes(dto.getBordes());
        entity.setDisponible(dto.getDisponible());
        
        return entity;
    }
	
	public List<PizzaDTO> toDtos(List<PizzaEntity> entities) {
		return entities.stream()
				.map(p -> this.toDto(p))
				.toList();
	}
	
	public List<PizzaEntity> toEntities(List<PizzaDTO> dtos) {
		return dtos.stream()
				.map(p -> this.toEntity(p))
				.toList();
	}	

}
