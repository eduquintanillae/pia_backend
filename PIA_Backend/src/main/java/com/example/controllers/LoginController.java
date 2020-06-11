package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.models.dao.UsuarioActualDao;
import com.example.models.dao.UsuarioDao;

@Controller
@RequestMapping(path = "")
public class LoginController {
	
	@Autowired
	private UsuarioActualDao usuarioActualDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
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
	
	@GetMapping({ "/ingresar" })
	public String ingresar(Model model) {
		//aquí hay que poner que reciba lo del form y hacer las validaciones
		//que exista el id dentro de la tabla de usuarios
		//que sea correcta la contraseña de ese id
		return "catalogo/login/menu";
	}
	
	@GetMapping({ "/menu" })
	public String menu() {
		return "catalogo/login/menu";
	}
}
