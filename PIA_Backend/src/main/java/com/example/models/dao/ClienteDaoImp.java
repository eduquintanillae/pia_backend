package com.example.models.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.entitys.Cliente;
import com.example.models.entitys.Prestamo;

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
	
	public Cliente findId(Long id){
		Cliente out = new Cliente();
		for (Cliente cliente : findAll()) {
			if (id == cliente.getId()) {
				out = cliente;
				break;
			}
		}
		return out;
	}
	
	@Override
	public List<Cliente> findNombre(String nombre) {
		System.out.println("PRUEBA: " + nombre);
		List<Cliente> list = new ArrayList<Cliente>();
		
		for(Cliente cliente : findAll()) {
			String nombre_completo = cliente.getNombre().concat(" ").concat(cliente.getApellido());
			System.out.println(nombre_completo);
			if(nombre_completo.contains(nombre)) {
				list.add(cliente);
			}
		}
		Collections.sort(list, new Comparator<Cliente>(){
            public int compare(Cliente o1, Cliente o2) {
                return o1.getNombre().compareToIgnoreCase(o2.getNombre());
            }
        });
		for(Cliente c : list) {
			System.out.println("Cliente: " + c.getNombre());
		}
		return list;
	}

}
