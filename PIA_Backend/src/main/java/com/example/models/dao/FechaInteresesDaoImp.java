package com.example.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.entitys.FechaIntereses;

@Repository
public class FechaInteresesDaoImp implements FechaInteresesDao{


	@PersistenceContext
	private EntityManager en;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<FechaIntereses> findAll() {
		List<FechaIntereses> result = en.createQuery("from FechaIntereses").getResultList();
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public FechaIntereses find(Long id) {
		FechaIntereses result = en.find(FechaIntereses.class, id);//select * from cliente where id ={id}
		return result;
	}

	@Override
	@Transactional
	public void insert(FechaIntereses nuevo) {
		if(nuevo.getId() != null && nuevo.getId() > 0) {
			en.merge(nuevo);
		}else {
			en.persist(nuevo);//insert into cliente (nombre, apellido, edad, fecha_nacimiento) values ('eduardo', 'guajardo', 24, '1995-11-26');
		}
		en.flush();
	}

	@Override
	@Transactional
	public void update(FechaIntereses nuevo) {
		FechaIntereses antes = find(nuevo.getId());
		BeanUtils.copyProperties(nuevo, antes);
		en.flush();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		FechaIntereses entity = find(id);
		en.remove(entity);
	}
	
	public FechaIntereses findId(Long id){
		FechaIntereses out = new FechaIntereses();
		for (FechaIntereses fecha : findAll()) {
			if (id == fecha.getId()) {
				out = fecha;
				break;
			}
		}
		return out;
	}

}
