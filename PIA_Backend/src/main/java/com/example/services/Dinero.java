package com.example.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.models.dao.ClienteDao;
import com.example.models.dao.FechaInteresesDao;
import com.example.models.dao.PrestamoDao;
import com.example.models.entitys.Cliente;
import com.example.models.entitys.FechaIntereses;
import com.example.models.entitys.Prestamo;
import com.example.services.interfaces.DineroInterface;

@Service
public class Dinero implements DineroInterface {
	
	@Autowired
	private ClienteDao clienteDao;
	
	@Autowired
	private PrestamoDao prestamoDao;
	
	@Autowired
	private FechaInteresesDao fechaDao;
	
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

	@Override
	public boolean abonar(Long id, Float abono) {
		Float excedente;
		Prestamo prestamo = prestamoDao.find(id);
		if(prestamo == null)
			return false;
		if (prestamo.getCliente().getMonto() < abono || abono < 0)
			return false;
		prestamo.setAbonoTotal(prestamo.getAbonoTotal() + abono);
		prestamo.getCliente().setMonto(prestamo.getCliente().getMonto() - abono);
		if (Float.compare(prestamo.getAbonoTotal(), prestamo.getMonto()) == 0)
			prestamo.setPagado(true);
		if (prestamo.getAbonoTotal() > prestamo.getMonto()) {
			prestamo.setPagado(true);
			excedente = prestamo.getAbonoTotal() - prestamo.getMonto();
			prestamo.setAbonoTotal(prestamo.getAbonoTotal() - excedente);
			prestamo.getCliente().setMonto(prestamo.getCliente().getMonto() + excedente);
			System.out.println("entre 2: ".concat(prestamo.getCliente().getMonto().toString()));
		}
		prestamoDao.update(prestamo);
		clienteDao.update(prestamo.getCliente());
		return true;
	}
	
	public void aplicarIntereses() {
		float interes[] = new float[4];
		interes[1] = (float) 0.05;
		interes[2] = (float) 0.1;
		interes[3] = (float) 0.3;
		Date date = new Date();
        Calendar inicio = new GregorianCalendar();
        Calendar fin = new GregorianCalendar();
        try {inicio.setTime(date);} 
        catch (Exception e) {return;}
		for(Prestamo prestamo : prestamoDao.findActivos()) {
			FechaIntereses fechaIP = fechaDao.findId(prestamo.getId());
			if(fechaIP.getId() == null) {
				fechaIP.setFechaInteres(prestamo.getFechaExpiracion());
				fechaIP.setId(prestamo.getId());
	            fechaDao.insert(fechaIP);
			}
            fin.setTime(fechaIP.getFechaInteres());
            int difA = inicio.get(Calendar.YEAR) - fin.get(Calendar.YEAR);
            int difM = difA * 12 + inicio.get(Calendar.MONTH) - fin.get(Calendar.MONTH);
            if(difM > 0) {
	            prestamo.setMonto(prestamo.getMonto() + prestamo.getMonto() * interes[prestamo.getTipo().intValue()] * difM);
	            fechaIP.setFechaInteres(date);
	            prestamoDao.update(prestamo);
	            fechaDao.update(fechaIP);
            }
		}
	}

	@Override
	public boolean abonarCliente(Long id, Float abono) {
		Float excedente;
		Prestamo prestamo = prestamoDao.find(id);
		System.out.println("Abono: "+abono+" Prestao.getMonto: "+prestamo.getMonto());
		if(prestamo.getPagado()==false) {
			if(abono>=prestamo.getMonto()) {
				System.out.println("Caso 1");
				prestamo.setAbonoTotal(prestamo.getMonto());
				prestamo.setPagado(true);
				excedente = abono - prestamo.getMonto();
				prestamo.getCliente().setMonto(prestamo.getCliente().getMonto() + excedente);
				prestamoDao.update(prestamo);
				clienteDao.update(prestamo.getCliente());
				return false;
			}else {
				System.out.println("Caso 2");
				prestamo.setAbonoTotal(prestamo.getAbonoTotal() + abono);
				prestamo.getCliente().setMonto(prestamo.getCliente().getMonto() - abono);
				prestamoDao.update(prestamo);
				clienteDao.update(prestamo.getCliente());
				return true;
			}
		}else {
			System.out.println("Caso 3");
			return false;
		}
	}
}
