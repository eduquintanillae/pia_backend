package com.example.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.entitys.Usuario;

@Repository
public class UsuarioDaoImp implements UsuarioDao {
	
	@PersistenceContext
	private EntityManager en;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		List<Usuario> result = en.createQuery("from Usuario").getResultList(); //select * from usuario
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario find(Long id) {
		Usuario result = en.find(Usuario.class, id);
		return result;
	}

	@Override
	@Transactional
	public void insert(Usuario nuevo) {
		if(nuevo.getId() != null && nuevo.getId() > 0) {
			en.merge(nuevo);
		}else {
			en.persist(nuevo);
		}
		en.flush();
	}

	@Override
	@Transactional
	public void update(Usuario nuevo) {
		Usuario antes = find(nuevo.getId());
		BeanUtils.copyProperties(nuevo, antes);
		en.flush();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		Usuario entity = find(id);
		en.remove(entity);
	}

}
