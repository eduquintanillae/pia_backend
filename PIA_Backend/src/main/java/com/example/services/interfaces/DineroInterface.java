package com.example.services.interfaces;

import com.example.models.entitys.Cliente;

public interface DineroInterface {

	public float montoTotal();
	public Cliente clienteMasAdinerado();
	public boolean abonar(Long id, Float abono);
	public void aplicarIntereses();
	public boolean abonarCliente(Long id, Float monto);
	public boolean abonarClienteCN(Long id, Float abono);
	
}
