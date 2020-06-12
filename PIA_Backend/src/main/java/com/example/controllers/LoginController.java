package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.models.dao.ClienteDao;
import com.example.models.dao.UsuarioActualDao;
import com.example.models.dao.UsuarioDao;
import com.example.models.entitys.Cliente;
import com.example.models.entitys.Usuario;

@Controller
@RequestMapping(path = "")
public class LoginController {
	
	@Autowired
	private UsuarioActualDao usuarioActualDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private ClienteDao clienteDao;
	
	@GetMapping({ "", "/" })
	public String index() {
		if(usuarioActualDao.estaConectado()) 
			return "redirect:/menu";
		else 
			return "redirect:/login";
	}
	
	@GetMapping({ "/login" })
	public String login(){
		return "catalogo/login/login";
	}
	
	@PostMapping({ "/ingresar" })
	public String ingresar(Long id, String password, Model model) {
		//aquí hay que poner que reciba lo del form y hacer las validaciones
		//que exista el id dentro de la tabla de usuarios
		//que sea correcta la contraseña de ese id
		System.out.println(id);
		System.out.println(password);
		if(id == null) {
			return "redirect:/login";
		}
		if(id == 0l) {
			Usuario uA = usuarioDao.find(0l);
			System.out.println(uA.getPassword());
			if(uA.getPassword().equals(password)) {
				usuarioActualDao.login(0l);
				return "redirect:/administrador";
			}
			else {
				return "redirect:/login";
			}
		}
		Cliente cliente = clienteDao.findId(id);
		if(cliente.getId() == null) {
			return "redirect:/login";
		}
		System.out.println(1);
		Usuario usuario = usuarioDao.find(id);
		System.out.println("Apunto" + cliente.getNombre());
		if(usuario.getPassword().equals(password)) {
			System.out.println("Entro" + cliente.getNombre());
			usuarioActualDao.login(id);
		}
		else {
			return "redirect:/login";
		}
		return "redirect:/cliente/menu";
	}

	@GetMapping({ "/logout" })
	public String logout(Model model) {
		System.out.println("logout1");
		usuarioActualDao.logout();
		return "redirect:/login";
	}
	
	@GetMapping({ "/menu" })
	public String menu() {
		return "catalogo/login/menu";
	}
}
