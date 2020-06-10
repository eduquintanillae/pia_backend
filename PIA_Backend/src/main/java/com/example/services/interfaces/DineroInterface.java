package com.example.services.interfaces;

import com.example.models.entitys.Cliente;

public interface DineroInterface {

	public float montoTotal();
	public Cliente clienteMasAdinerado();
	public boolean abonar(Long id, Float abono);
	
}
