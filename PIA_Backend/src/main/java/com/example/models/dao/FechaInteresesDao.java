package com.example.models.dao;

import java.util.List;

import com.example.models.entitys.FechaIntereses;

public interface FechaInteresesDao {
	
	public List<FechaIntereses> findAll();
	FechaIntereses find(Long id);
	void insert(FechaIntereses nuevo);
	void update(FechaIntereses nuevo);// debatible
	void delete(Long id);
	public FechaIntereses findId(Long id);
	
}
