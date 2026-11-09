package com.daw.web.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.services.PizzaPedidoService;
import com.daw.services.dtos.PizzaPedidoDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pizza_pedidos")
@RequiredArgsConstructor
public class PizzaPedidoController {
	
	private final PizzaPedidoService pizzaPedidoService;
	
	@GetMapping
	public ResponseEntity<List<PizzaPedidoDto>> list(){
		return ResponseEntity.ok(this.pizzaPedidoService.findAll());
	}
}
