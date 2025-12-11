package com.daw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Direccion;
import com.daw.persistence.repositories.DireccionRepository;
import com.daw.services.exceptions.ClienteNotFoundException;
import com.daw.services.exceptions.DireccionException;
import com.daw.services.exceptions.DireccionNotFoundException;

@Service
public class DireccionService {

	@Autowired
	private DireccionRepository direccionRepository;

	@Autowired
	private ClienteService clienteService;

	public List<Direccion> findAll() {
		return this.direccionRepository.findAll();
	}

	public Direccion findById(int idDireccion) {
		if (!this.direccionRepository.existsById(idDireccion)) {
			throw new DireccionNotFoundException("El ID indicado no existe. ");
		}
		
		return this.direccionRepository.findById(idDireccion).get();
	}

	public Direccion create(Direccion direccion) {
		if(!this.clienteService.existsCliente(direccion.getIdCliente())) {
			throw new ClienteNotFoundException("El cliente no existe. ");
		}
		
		direccion.setId(0);
		
		marcarInactivas(direccion.getIdCliente());
		
		direccion.setActiva(true);

		return this.direccionRepository.save(direccion);
	}

	public Direccion update(int idDireccion, Direccion direccion) {
		if(idDireccion != direccion.getId()) {
			throw new DireccionNotFoundException("El ID del path y del body no coinciden. ");
		}
		if(direccion.getActiva() != null) {
			throw new DireccionException("Para marcar/desmarcar activa una dirección se debe usar el endpoint específico. ");
		}
		
		Direccion direccionBD = this.findById(idDireccion);
		direccionBD.setIdCliente(direccion.getIdCliente());
		direccionBD.setCalle(direccion.getCalle());
		direccionBD.setNumero(direccion.getNumero());
		direccionBD.setPoblacion(direccion.getPoblacion());		
		
		return this.direccionRepository.save(direccionBD);
	}

	public void delete(int idDireccion) {
		if (!this.direccionRepository.existsById(idDireccion)) {
			throw new DireccionNotFoundException("El ID indicado no existe. ");
		}
		
		this.direccionRepository.deleteById(idDireccion);
	}

	public void marcarInactivas(int idCliente) {
		if(!this.clienteService.existsCliente(idCliente)) {
			throw new ClienteNotFoundException("El cliente no existe. ");
		}
		
		List<Direccion> direcciones = this.direccionRepository.findByIdCliente(idCliente);

		for (Direccion dir : direcciones) {
			dir.setActiva(false);
			this.direccionRepository.save(dir);
		}
	}

	public Direccion marcarDesmarcarActivas(int idDireccion) {
		Direccion direccion = this.findById(idDireccion);
		
		if(direccion.getActiva()) {
			direccion.setActiva(false);
		}
		else {
			marcarInactivas(direccion.getIdCliente());
			direccion.setActiva(true);
		}
		
		return this.direccionRepository.save(direccion);
	}
	
}
