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

import com.daw.services.PedidoService;
import com.daw.services.dtos.PedidoInputDTO;
import com.daw.services.dtos.PedidoOutputDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

	private final PedidoService pedidoService;

	@GetMapping
	public ResponseEntity<List<PedidoOutputDTO>> list() {
		return ResponseEntity.ok(this.pedidoService.findAll());
	}

	@GetMapping("/{idPedido}")
	public ResponseEntity<PedidoOutputDTO> findById(@PathVariable long idPedido) {
		return ResponseEntity.ok(this.pedidoService.findById(idPedido));
	}

	@PostMapping
	public ResponseEntity<PedidoOutputDTO> create(@Valid @RequestBody PedidoInputDTO inputDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.create(inputDto));
	}

	@PutMapping("/{idPedido}")
	public ResponseEntity<PedidoOutputDTO> update(@PathVariable long idPedido, @Valid @RequestBody PedidoInputDTO inputDto) {
		return ResponseEntity.ok(this.pedidoService.update(idPedido, inputDto));
	}

	@DeleteMapping("/{idPedido}")
	public ResponseEntity<PedidoOutputDTO> delete(@PathVariable long idPedido) {
		this.pedidoService.deleteById(idPedido);
		return ResponseEntity.ok().build();
	}

}
