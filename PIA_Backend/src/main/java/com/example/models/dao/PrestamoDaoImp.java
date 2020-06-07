package com.example.models.dao;

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

}
