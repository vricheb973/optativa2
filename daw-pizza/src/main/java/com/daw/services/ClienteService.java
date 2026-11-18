package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.daw.persistence.entities.ClienteEntity;
import com.daw.persistence.repositories.ClienteRepository;
import com.daw.services.dtos.ClienteDTO;
import com.daw.services.exceptions.BusinessRuleException;
import com.daw.services.exceptions.ResourceNotFoundException;
import com.daw.services.mappers.ClienteMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class ClienteService {
	
	private final ClienteRepository clienteRepository;
	private final ClienteMapper clienteMapper;
	
	public List<ClienteDTO> findAll(){
		return this.clienteMapper.toDtos(this.clienteRepository.findAll());
	}
	
	public ClienteDTO findById(long idCliente) {
		if (!this.clienteRepository.existsById(idCliente)) {
			throw new ResourceNotFoundException("Cliente no encontrado con ID: " + idCliente);
		}

		return this.clienteMapper.toDto(this.clienteRepository.findById(idCliente).get());
	}
	
	public ClienteDTO create(@Valid ClienteDTO dto) {
		ClienteEntity entity = this.clienteMapper.toEntity(dto);
		
		entity.setId(null);
		
		if(this.clienteRepository.existsByEmail(entity.getEmail())) {
			throw new BusinessRuleException("Ya existe un cliente registrado con el email: " + entity.getEmail());
		}
		if(this.clienteRepository.existsByTelefono(entity.getTelefono())) {
            throw new BusinessRuleException("Ya existe un cliente registrado con el teléfono: " + entity.getTelefono());
        }

		return this.clienteMapper.toDto(this.clienteRepository.save(entity));
	}
	
	public ClienteDTO update(int idCliente, @Valid ClienteDTO dto) {
		ClienteEntity clienteBD = this.findEntityById(idCliente);
		
        // Validar email único si cambió
        if (!dto.getEmail().equalsIgnoreCase(clienteBD.getEmail()) && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("Ya existe otro cliente con el email: " + dto.getEmail());
        }
        // Validar teléfono único si cambió
        if (!dto.getTelefono().equals(clienteBD.getTelefono()) && clienteRepository.existsByTelefono(dto.getTelefono())) {
            throw new BusinessRuleException("Ya existe otro cliente con el teléfono: " + dto.getTelefono());
        }
		
		clienteBD.setNombre(dto.getNombre());
		clienteBD.setDireccion(dto.getDireccion());
		clienteBD.setEmail(dto.getEmail());
		clienteBD.setTelefono(dto.getTelefono());

		return this.clienteMapper.toDto(this.clienteRepository.save(clienteBD));
	}
	
	public void deleteById(long idCliente) {
		if (!this.clienteRepository.existsById(idCliente)) {
			throw new ResourceNotFoundException("No se puede eliminar. Cliente no encontrado con ID: " + idCliente);
		}

		this.clienteRepository.deleteById(idCliente);
	}
	
	//Métodos que devuelven entities
	public ClienteEntity findEntityById(long idCliente) {
		if (!this.clienteRepository.existsById(idCliente)) {
			throw new ResourceNotFoundException("Cliente no encontrado con ID: " + idCliente);
		}

		return this.clienteRepository.findById(idCliente).get();
	}
	

}
