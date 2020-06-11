package com.example.models.dao;

public interface UsuarioActualDao {
	//Este Dao sólo modifica el registro único de la tabla, ya que se utiliza como si fuera una variable global
	Long get();
	boolean estaConectado();
	boolean esAdmin();
	void login(Long id);
	void logout();	
}
