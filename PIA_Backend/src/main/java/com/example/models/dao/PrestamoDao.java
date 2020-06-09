package com.example.models.dao;

import java.util.Date;
import java.util.List;

import com.example.models.entitys.Prestamo;

public interface PrestamoDao {

	List<Prestamo> findAll();
	Prestamo find(Long id);
	void insert(Prestamo nuevo);
	void update(Prestamo nuevo);// debatible
	void delete(Long id);
	public List<Prestamo> findIdCliente(Long id);
	public List<Prestamo> findFecha(Date fecha1, Date fecha2);
	public List<Prestamo> findActivos();
	public List<Prestamo> findPagados();
}
