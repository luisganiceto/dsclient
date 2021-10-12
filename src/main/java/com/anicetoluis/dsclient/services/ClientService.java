package com.anicetoluis.dsclient.services;


import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anicetoluis.dsclient.dto.ClientDTO;
import com.anicetoluis.dsclient.entities.Client;
import com.anicetoluis.dsclient.repositories.ClienteRepository;
import com.anicetoluis.dsclient.services.exceptions.DataBaseException;
import com.anicetoluis.dsclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClienteRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);		
		return list.map(x -> new ClientDTO(x));		
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> optional = repository.findById(id);
		Client client = optional.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));		
		return new ClientDTO(client);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client client = new Client();
		copyDtoToEntity(dto, client);
		client = repository.save(client);
		return new ClientDTO(client);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client client = repository.getOne(id);
			copyDtoToEntity(dto, client);
			client = repository.save(client);
			return new ClientDTO(client);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id " + id + " Not Found");
		}		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id " + id + " Not Found");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}		
	}
	
	private void copyDtoToEntity(ClientDTO dto, Client client) {
		client.setName(dto.getName());
		client.setCpf(dto.getCpf());
		client.setIncome(dto.getIncome());
		client.setBirthDate(dto.getBirthDate());
		client.setChildren(dto.getChildren());
	}
}
