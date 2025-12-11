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
import com.daw.services.exceptions.ClienteNotFoundException;
import com.daw.services.exceptions.DireccionException;
import com.daw.services.exceptions.DireccionNotFoundException;

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
		try {
			return ResponseEntity.ok(this.direccionService.findById(idDireccion));
		} catch (DireccionNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Direccion direccion) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.direccionService.create(direccion));
		} catch (ClienteNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@PutMapping("/{idDireccion}")
	public ResponseEntity<?> update(@PathVariable int idDireccion, @RequestBody Direccion direccion) {
		try {
			return ResponseEntity.ok(this.direccionService.update(idDireccion, direccion));
		} catch (DireccionNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch (DireccionException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}

	@DeleteMapping("/{idDireccion}")
	public ResponseEntity<?> delete(@PathVariable int idDireccion) {
		try {
			this.direccionService.delete(idDireccion);
			return ResponseEntity.ok().build();
		}
		catch(DireccionNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@PutMapping("/{idDireccion}/activa")
	public ResponseEntity<?> marcarDesmarcarActiva(@PathVariable int idDireccion) {
		try {
			return ResponseEntity.ok(this.direccionService.marcarDesmarcarActivas(idDireccion));
		} catch (DireccionNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch (ClienteNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

}
