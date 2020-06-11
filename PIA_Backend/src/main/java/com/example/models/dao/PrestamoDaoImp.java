package com.example.models.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.entitys.Prestamo;

@Repository
public class PrestamoDaoImp implements PrestamoDao {

	@PersistenceContext
	private EntityManager en;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Prestamo> findAll() {
		/*HQL o JPQL != SQL*/
		List<Prestamo> result = en.createQuery("from Prestamo").getResultList();//select * from cliente
		return result;
//		return en.createQuery("from Prestamo").getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Prestamo find(Long id) {
		Prestamo result = en.find(Prestamo.class, id);//select * from cliente where id ={id}
		return result;
	}

	@Override
	@Transactional
	//Deberia llamarse guardar
	public void insert(Prestamo nuevo) {
		
		
		nuevo.setPagado(false);
		nuevo.setAbonoTotal((float)0.0);
		nuevo.setFechaCreacion(new Date());
		Calendar c = Calendar.getInstance();
        c.setTime(nuevo.getFechaCreacion());
        int x = nuevo.getTipo().intValue();
        c.add(Calendar.MONTH, x);
		nuevo.setFechaExpiracion(c.getTime());
		
		if(nuevo.getTipo() < 3) {
			nuevo.setTipo((long) 1); 
		} else if(nuevo.getTipo() < 7) {
			nuevo.setTipo((long)2); 
		} else {
			nuevo.setTipo((long)3); 
		}
		
		if(nuevo.getId() != null && nuevo.getId() > 0) {
			en.merge(nuevo);
		}else {
			en.persist(nuevo);//insert into cliente (nombre, apellido, edad, fecha_nacimiento) values ('eduardo', 'guajardo', 24, '1995-11-26');
		}
		en.flush();
	}

	@Override
	@Transactional
	public void update(Prestamo nuevo) {
		Prestamo antes = find(nuevo.getId());
		//direccion de contexto nuevo es otra direccion
		BeanUtils.copyProperties(nuevo, antes);
//		antes.setNombre(nuevo.getNombre());
//		antes.setApellido(nuevo.getApellido());
//		antes.setEdad(nuevo.getEdad());
//		antes.setFechaNacimiento(nuevo.getFechaNacimiento());
		en.flush();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		Prestamo entity = find(id);
//		en.remove(find(id));
		en.remove(entity);
	}
	
	public List<Prestamo> findIdCliente(Long id){
		List<Prestamo> out = new ArrayList<Prestamo>();
		for (Prestamo prestamo : findAll()) {
			if (id == prestamo.getCliente().getId())
				out.add(prestamo);
		}
		return out;
	}
	
	public List<Prestamo> findFecha(Date fecha1, Date fecha2){
		List<Prestamo> out = new ArrayList<Prestamo>();
		for (Prestamo prestamo : findAll()) {
			if ((prestamo.getFechaCreacion().after(fecha1) && prestamo.getFechaCreacion().before(fecha2)) 
					|| prestamo.getFechaCreacion().equals(fecha1) || prestamo.getFechaExpiracion().equals(fecha2)
					|| prestamo.getFechaExpiracion().equals(fecha1) || prestamo.getFechaCreacion().equals(fecha1))
				out.add(prestamo);
		}
		return out;
	}
	
	public List<Prestamo> findActivos(){
		List<Prestamo> out = new ArrayList<Prestamo>();
		for (Prestamo prestamo : findAll()) {
			if (!prestamo.getPagado())
				out.add(prestamo);
		}
		return out;
	}
	
	public List<Prestamo> findPagados(){
		List<Prestamo> out = new ArrayList<Prestamo>();
		for (Prestamo prestamo : findAll()) {
			if (prestamo.getPagado())
				out.add(prestamo);
		}
		return out;
	}

}
