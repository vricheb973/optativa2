package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.PizzaEntity;
import com.daw.persistence.repositories.PizzaRepository;
import com.daw.services.exceptions.PizzaNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaService {
	
	private final PizzaRepository pizzaRepository;
	
	public List<PizzaEntity> findAll(){
		return this.pizzaRepository.findAll();
	}
	
	public PizzaEntity findById(long idPizza) {
		if(!this.pizzaRepository.existsById(idPizza)) {
			throw new PizzaNotFoundException("El ID indicado no existe. ");
		}
		
		return this.pizzaRepository.findById(idPizza).get();
	}
	
	public PizzaEntity create(PizzaEntity pizza) {		
		if(pizza.getDisponible() == null) {
			pizza.setDisponible(true);
		}
		
		pizza.setId(0L);
		
		return this.pizzaRepository.save(pizza);
	}
	
	public PizzaEntity update(long idPizza, PizzaEntity pizzaEntity) {
		if(idPizza != pizzaEntity.getId()) {
			throw new PizzaNotFoundException("El ID del path y del body no coinciden. ");
		}
		
		PizzaEntity pizzaBD = this.findById(idPizza);
		pizzaBD.setDescripcion(pizzaEntity.getDescripcion());
		pizzaBD.setDisponible(pizzaEntity.getDisponible());
		pizzaBD.setNombre(pizzaEntity.getNombre());
		pizzaBD.setPrecio(pizzaEntity.getPrecio());
		pizzaBD.setVegana(pizzaEntity.isVegana());
		pizzaBD.setVegetariana(pizzaEntity.isVegetariana());
		
		return this.pizzaRepository.save(pizzaBD);
	}
	
	public void deleteById(long idPizza) {
		if(!this.pizzaRepository.existsById(idPizza)) {
			throw new PizzaNotFoundException("El ID indicado no existe. ");
		}
		
		this.pizzaRepository.deleteById(idPizza);
	}

}
