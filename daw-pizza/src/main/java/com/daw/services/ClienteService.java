package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.ClienteEntity;
import com.daw.persistence.repositories.ClienteRepository;
import com.daw.services.exceptions.ClienteException;
import com.daw.services.exceptions.ClienteNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
	
	private final ClienteRepository clienteRepository;
	
	public List<ClienteEntity> findAll(){
		return this.clienteRepository.findAll();
	}
	
	public ClienteEntity findById(long idCliente) {
		if (!this.clienteRepository.existsById(idCliente)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}

		return this.clienteRepository.findById(idCliente).get();
	}
	
	public ClienteEntity create(ClienteEntity cliente) {
		cliente.setId(0L);

		return this.clienteRepository.save(cliente);
	}
	
	public ClienteEntity update(int idCliente, ClienteEntity cliente) {
		if (idCliente != cliente.getId()) {
			throw new ClienteException("El ID del path y del body no coinciden. ");
		}

		ClienteEntity clienteBD = this.findById(idCliente);
		clienteBD.setNombre(cliente.getNombre());
		clienteBD.setDireccion(cliente.getDireccion());
		clienteBD.setEmail(cliente.getEmail());
		clienteBD.setTelefono(cliente.getTelefono());

		return this.clienteRepository.save(clienteBD);
	}
	
	public void deleteById(long idCliente) {
		if (!this.clienteRepository.existsById(idCliente)) {
			throw new ClienteNotFoundException("El ID indicado no existe. ");
		}

		this.clienteRepository.deleteById(idCliente);
	}
	

}
