package com.daw.web.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.PizzaEntity;
import com.daw.services.PizzaService;
import com.daw.services.exceptions.PizzaException;
import com.daw.services.exceptions.PizzaNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pizzas")
@RequiredArgsConstructor
public class PizzaController {

	private final PizzaService pizzaService;

	@GetMapping
	public ResponseEntity<List<PizzaEntity>> list() {
		return ResponseEntity.ok(this.pizzaService.findAll());
	}

	@GetMapping("/{idPizza}")
	public ResponseEntity<?> findById(@PathVariable long idPizza) {
		try {
			return ResponseEntity.ok(this.pizzaService.findById(idPizza));
		} catch (PizzaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody PizzaEntity pizza) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.pizzaService.create(pizza));
	}

	@PutMapping("/{idPizza}")
	public ResponseEntity<?> update(@PathVariable long idPizza, @RequestBody PizzaEntity pizza) {
		try {
			return ResponseEntity.ok(this.pizzaService.update(idPizza, pizza));

		} catch (PizzaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch (PizzaException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}

	@DeleteMapping("/{idPizza}")
	public ResponseEntity<?> delete(@PathVariable long idPizza) {
		try {
			this.pizzaService.deleteById(idPizza);
			return ResponseEntity.ok().build();

		} catch (PizzaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}
}
