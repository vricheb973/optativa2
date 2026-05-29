package com.daw.web.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daw.services.exceptions.ClienteException;
import com.daw.services.exceptions.ClienteNotFoundException;
import com.daw.services.exceptions.DireccionException;
import com.daw.services.exceptions.DireccionNotFoundException;
import com.daw.services.exceptions.PedidoException;
import com.daw.services.exceptions.PedidoNotFoundException;
import com.daw.services.exceptions.PizzaException;
import com.daw.services.exceptions.PizzaNotFoundException;
import com.daw.services.exceptions.PizzaPedidoException;
import com.daw.services.exceptions.PizzaPedidoNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ClienteException.class)
    public ResponseEntity<String> handleBadRequest(ClienteException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
	
	@ExceptionHandler(DireccionException.class)
    public ResponseEntity<String> handleBadRequest(DireccionException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

	@ExceptionHandler(PedidoException.class)
    public ResponseEntity<String> handleBadRequest(PedidoException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
	
	@ExceptionHandler(PizzaException.class)
    public ResponseEntity<String> handleBadRequest(PizzaException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

	@ExceptionHandler(PizzaPedidoException.class)
    public ResponseEntity<String> handleBadRequest(PizzaPedidoException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
	

	@ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ClienteNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
	
	@ExceptionHandler(DireccionNotFoundException.class)
    public ResponseEntity<String> handleNotFound(DireccionNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

	@ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<String> handleNotFound(PedidoNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
	
	@ExceptionHandler(PizzaNotFoundException.class)
    public ResponseEntity<String> handleNotFound(PizzaNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

	@ExceptionHandler(PizzaPedidoNotFoundException.class)
    public ResponseEntity<String> handleNotFound(PizzaPedidoNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
	
}
