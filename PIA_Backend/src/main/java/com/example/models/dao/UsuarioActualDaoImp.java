package com.example.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.entitys.UsuarioActual;

@Repository
public class UsuarioActualDaoImp implements UsuarioActualDao {
	
	@PersistenceContext
	private EntityManager en;
	private UsuarioActual usuario;

	//Regresar el id del usuario conectado
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Long get() {
		return en.find(UsuarioActual.class, 1).getIdUsuario();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public boolean estaConectado() {
		return en.find(UsuarioActual.class, 1).isConectado();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public boolean esAdmin() {
		Long id = en.find(UsuarioActual.class, 1).getIdUsuario();
		if(id==0)
			return true;
		else
			return false;
	}
	
	@Override
	@Transactional
	public void login(Long id) {
		System.out.println(31);
		//Tener un objeto tipo UsuarioActual e inicializarlo como conectado y con el id del login
		UsuarioActual u = new UsuarioActual();
		u.setIdTabla(1);
		u.setIdUsuario(id);
		u.setConectado(true);
		//Reemplazar el registro existente de UsuarioActual (siempre id 1) con el nuevo
		UsuarioActual antes = en.find(UsuarioActual.class, 1);
		BeanUtils.copyProperties(u, antes);

	}

	@Override
	@Transactional
	public void logout() {
		//Tener un objeto tipo UsuarioActual e inicializarlo como desconectado y sin id de usuario
		UsuarioActual u = new UsuarioActual();
		u.setIdTabla(1);
		u.setIdUsuario(null);
		u.setConectado(false);
		//Reemplazar el registro existente de UsuarioActual (siempre id 1) con el nuevo
		UsuarioActual antes = en.find(UsuarioActual.class, 1);
		BeanUtils.copyProperties(usuario, antes);
	}

}
