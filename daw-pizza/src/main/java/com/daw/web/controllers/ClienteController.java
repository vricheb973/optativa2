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

import com.daw.persistence.entities.Cliente;
import com.daw.services.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	public ResponseEntity<List<Cliente>> list() {
		return ResponseEntity.ok(this.clienteService.findAll());
	}

	@GetMapping("/{idCliente}")
	public ResponseEntity<?> findById(@PathVariable int idCliente) {
		return ResponseEntity.ok(this.clienteService.findDTOById(idCliente));
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.clienteService.create(cliente));
	}

	@PutMapping("/{idCliente}")
	public ResponseEntity<?> update(@PathVariable int idCliente, @RequestBody Cliente cliente) {
		return ResponseEntity.ok(this.clienteService.update(idCliente, cliente));
	}

	@DeleteMapping("/{idCliente}")
	public ResponseEntity<?> delete(@PathVariable int idCliente) {
		this.clienteService.deleteById(idCliente);
		return ResponseEntity.ok().build();
	}

}
