package com.example.models.dao;

import java.util.List;

import com.example.models.entitys.Usuario;

public interface UsuarioDao {

	List<Usuario> findAll();
	Usuario find(Long id);
	void insert(Usuario nuevo);
	void update(Usuario nuevo);
	void delete(Long id);
}
