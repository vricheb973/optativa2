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

import com.daw.persistence.entities.Direccion;
import com.daw.services.DireccionService;

@RestController
@RequestMapping("/direcciones")
public class DireccionController {

	@Autowired
	private DireccionService direccionService;

	@GetMapping
	public ResponseEntity<List<Direccion>> list() {
		return ResponseEntity.ok(this.direccionService.findAll());
	}

	@GetMapping("/{idDireccion}")
	public ResponseEntity<?> findById(@PathVariable int idDireccion) {
		return ResponseEntity.ok(this.direccionService.findById(idDireccion));
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Direccion direccion) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.direccionService.create(direccion));
	}

	@PutMapping("/{idDireccion}")
	public ResponseEntity<?> update(@PathVariable int idDireccion, @RequestBody Direccion direccion) {
		return ResponseEntity.ok(this.direccionService.update(idDireccion, direccion));
	}

	@DeleteMapping("/{idDireccion}")
	public ResponseEntity<?> delete(@PathVariable int idDireccion) {
		this.direccionService.delete(idDireccion);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{idDireccion}/activa")
	public ResponseEntity<?> marcarDesmarcarActiva(@PathVariable int idDireccion) {
		return ResponseEntity.ok(this.direccionService.marcarDesmarcarActivas(idDireccion));
	}

}
