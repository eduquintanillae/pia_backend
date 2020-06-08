package com.example.models.dao;

import java.util.List;

import com.example.models.entitys.Cliente;

public interface ClienteDao {

	List<Cliente> findAll();
	Cliente find(Long id);
	void insert(Cliente nuevo);
	void update(Cliente nuevo);// debatible
	void delete(Long id);
	Cliente findNombre(String id);
}
