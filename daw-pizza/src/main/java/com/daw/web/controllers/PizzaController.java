package com.daw.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.daw.persistence.entities.Pizza;
import com.daw.services.PizzaService;

@RestController
@RequestMapping("/pizzas")
public class PizzaController {

	@Autowired
	private PizzaService pizzaService;

	@GetMapping
	public ResponseEntity<List<Pizza>> list() {
		return ResponseEntity.ok(this.pizzaService.findAll());
	}

	@GetMapping("/{idPizza}")
	public ResponseEntity<?> findById(@PathVariable int idPizza) {
		return ResponseEntity.ok(this.pizzaService.findById(idPizza));
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Pizza pizza) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.pizzaService.create(pizza));
	}

	@PutMapping("/{idPizza}")
	public ResponseEntity<?> update(@PathVariable int idPizza, @RequestBody Pizza pizza) {
		return ResponseEntity.ok(this.pizzaService.update(idPizza, pizza));
	}

	@DeleteMapping("/{idPizza}")
	public ResponseEntity<?> delete(@PathVariable int idPizza) {
		this.pizzaService.deleteById(idPizza);
		return ResponseEntity.ok().build();
	}

}
