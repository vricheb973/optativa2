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

import com.daw.services.ClienteService;
import com.daw.services.dtos.ClienteDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;

	@GetMapping
	public ResponseEntity<List<ClienteDTO>> list() {
		return ResponseEntity.ok(this.clienteService.findAll());
	}

	@GetMapping("/{idCliente}")
	public ResponseEntity<ClienteDTO> findById(@PathVariable long idCliente) {
		return ResponseEntity.ok(this.clienteService.findById(idCliente));
	}

	@PostMapping
	public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.clienteService.create(dto));
	}

	@PutMapping("/{idCliente}")
	public ResponseEntity<ClienteDTO> update(@PathVariable int idCliente, @Valid @RequestBody ClienteDTO dto) {
		return ResponseEntity.ok(this.clienteService.update(idCliente, dto));
	}

	@DeleteMapping("/{idCliente}")
	public ResponseEntity<ClienteDTO> delete(@PathVariable int idCliente) {
		this.clienteService.deleteById(idCliente);
		return ResponseEntity.ok().build();
	}
}
