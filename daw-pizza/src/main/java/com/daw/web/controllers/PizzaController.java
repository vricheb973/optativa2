package com.daw.web.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.PizzaEntity;
import com.daw.services.PizzaService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/pizzas")
@RequiredArgsConstructor
public class PizzaController {

	private final PizzaService pizzaService;
	
	@GetMapping
	public ResponseEntity<List<PizzaEntity>> list(){
		return ResponseEntity.ok(this.pizzaService.findAll());
	}
}
