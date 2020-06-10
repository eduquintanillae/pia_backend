package com.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.models.dao.ClienteDao;
import com.example.models.entitys.Cliente;
import com.example.services.interfaces.DineroInterface;

@Service
public class Dinero implements DineroInterface {
	
	@Autowired
	private ClienteDao clienteDao;
	
	@Override
	public float montoTotal() {
		float suma = 0;
		for (Cliente cliente : clienteDao.findAll())
			suma += cliente.getMonto();
		return suma;
	}

	@Override
	public Cliente clienteMasAdinerado() {
		Cliente cliente = new Cliente();
		cliente.setMonto((float) 0.0);
		for(Cliente aux : clienteDao.findAll())
			if(cliente.getMonto() < aux.getMonto())
				cliente = aux;
		return cliente;
	}

}
