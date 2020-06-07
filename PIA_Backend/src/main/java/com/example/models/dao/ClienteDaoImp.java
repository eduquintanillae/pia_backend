package com.example.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.entitys.Cliente;

@Repository
public class ClienteDaoImp implements ClienteDao {
	
	@PersistenceContext
	private EntityManager en;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		/*HQL o JPQL != SQL*/
		List<Cliente> result = en.createQuery("from Cliente").getResultList();//select * from cliente
		return result;
//		return en.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente find(Long id) {
		Cliente result = en.find(Cliente.class, id);//select * from cliente where id ={id}
		return result;
	}

	@Override
	@Transactional
	//Deberia llamarse guardar
	public void insert(Cliente nuevo) {
		if(nuevo.getId() != null && nuevo.getId() > 0) {
			en.merge(nuevo);
		}else {
			en.persist(nuevo);//insert into cliente (nombre, apellido, edad, fecha_nacimiento) values ('eduardo', 'guajardo', 24, '1995-11-26');
		}
		en.flush();
	}

	@Override
	@Transactional
	public void update(Cliente nuevo) {
		Cliente antes = find(nuevo.getId());
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
		Cliente entity = find(id);
//		en.remove(find(id));
		en.remove(entity);
	}

}
