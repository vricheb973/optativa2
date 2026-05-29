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

import com.daw.persistence.entities.Pedido;
import com.daw.services.PedidoService;
import com.daw.services.dto.PedidoDTO;
import com.daw.services.dto.PizzaPedidoInputDTO;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	// CRUDs de Pedido
	@GetMapping
	public ResponseEntity<List<PedidoDTO>> list() {
		return ResponseEntity.ok(this.pedidoService.findAll());
	}

	@GetMapping("/{idPedido}")
	public ResponseEntity<?> findById(@PathVariable int idPedido) {
		return ResponseEntity.ok(this.pedidoService.findById(idPedido));
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Pedido pedido) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.create(pedido));
	}

	@PutMapping("/{idPedido}")
	public ResponseEntity<?> update(@PathVariable int idPedido, @RequestBody Pedido pedido) {
		return ResponseEntity.ok(this.pedidoService.update(idPedido, pedido));
	}

	@DeleteMapping("/{idPedido}")
	public ResponseEntity<?> delete(@PathVariable int idPedido) {
		this.pedidoService.deleteById(idPedido);
		return ResponseEntity.ok().build();
	}

	// CRUDs de PizzaPedido
	// findAll
	@GetMapping("/{idPedido}/pizzas")
	public ResponseEntity<?> listPizzaPedido(@PathVariable int idPedido) {
		return ResponseEntity.ok(this.pedidoService.findPizzasByIdPedido(idPedido));
	}

	// findById
	@GetMapping("/{idPedido}/pizzas/{idPizzaPedido}")
	public ResponseEntity<?> findPizzaPedidoById(@PathVariable int idPedido, @PathVariable int idPizzaPedido) {
		return ResponseEntity.ok(this.pedidoService.findPizzaPedidoById(idPedido, idPizzaPedido));

	}

	// create
	@PostMapping("/{idPedido}/pizzas")
	public ResponseEntity<?> create(@PathVariable int idPedido, @RequestBody PizzaPedidoInputDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoService.createPizzaPedido(idPedido, dto));
	}

	// update
	@PutMapping("/{idPedido}/pizzas/{idPizzaPedido}")
	public ResponseEntity<?> update(@PathVariable int idPedido, @PathVariable int idPizzaPedido,
			@RequestBody PizzaPedidoInputDTO dto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(this.pedidoService.updatePizzaPedido(idPedido, idPizzaPedido, dto));
	}

	// delete
	@DeleteMapping("/{idPedido}/pizzas/{idPizzaPedido}")
	public ResponseEntity<?> delete(@PathVariable int idPedido, @PathVariable int idPizzaPedido) {
		this.pedidoService.deletePizzaPedidoById(idPedido, idPizzaPedido);
		return ResponseEntity.ok().build();
	}

}
