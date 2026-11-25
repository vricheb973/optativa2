package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.daw.persistence.entities.PizzaEntity;
import com.daw.persistence.repositories.PizzaRepository;
import com.daw.services.dtos.PizzaDTO;
import com.daw.services.exceptions.ResourceNotFoundException;
import com.daw.services.mappers.PizzaMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class PizzaService {
	
	private final PizzaRepository pizzaRepository;
	private final PizzaMapper pizzaMapper;
	
	public List<PizzaDTO> findAll(){
		return this.pizzaMapper.toDtos(this.pizzaRepository.findAll());
	}
	
	public PizzaDTO findById(long idPizza) {
		if(!this.pizzaRepository.existsById(idPizza)) {
			throw new ResourceNotFoundException("Pizza no encontrada con ID: " + idPizza);
		}
		
		return this.pizzaMapper.toDto(this.pizzaRepository.findById(idPizza).get());
	}
	
	public PizzaDTO create(@Valid PizzaDTO dto) {		
		if(dto.getDisponible() == null) {
			dto.setDisponible(true);
		}	
		if(dto.getVegana() == null) {
			dto.setVegana(false);
		}	
		if(dto.getVegetariana() == null) {
			dto.setVegetariana(false);
		}
		
		PizzaEntity entity = this.pizzaMapper.toEntity(dto);
		entity.setId(null);
		
		return this.pizzaMapper.toDto(this.pizzaRepository.save(entity));
	}
	
	public PizzaDTO update(long idPizza, @Valid PizzaDTO inputDto) {		
		
		PizzaEntity pizzaBD = this.pizzaRepository.findById(idPizza)
				.orElseThrow(() -> new ResourceNotFoundException("Pizza no encontrada con ID: " + idPizza));
		
		pizzaBD.setDescripcion(inputDto.getDescripcion());
		pizzaBD.setDisponible(inputDto.getDisponible());
		pizzaBD.setNombre(inputDto.getNombre());
		pizzaBD.setPrecio(inputDto.getPrecio());
		pizzaBD.setVegana(inputDto.getVegana());
		pizzaBD.setVegetariana(inputDto.getVegetariana());
		
		return this.pizzaMapper.toDto(this.pizzaRepository.save(pizzaBD));
	}
	
	public void deleteById(long idPizza) {
		if(!this.pizzaRepository.existsById(idPizza)) {
			throw new ResourceNotFoundException("No se puede eliminar. Pizza no encontrada con ID: " + idPizza);
		}
		
		this.pizzaRepository.deleteById(idPizza);
	}

}
