package com.example.models.dao;

import java.util.List;

import com.example.models.entitys.Prestamo;

public interface PrestamoDao {

	List<Prestamo> findAll();
	Prestamo find(Long id);
	void insert(Prestamo nuevo);
	void update(Prestamo nuevo);// debatible
	void delete(Long id);
}
