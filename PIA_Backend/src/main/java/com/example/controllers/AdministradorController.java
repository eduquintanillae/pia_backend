package com.example.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.models.dao.ClienteDao;
import com.example.models.entitys.Cliente;

@Controller
@RequestMapping(path = "/administrador")
@SessionAttributes("cliente")
public class AdministradorController {

	@Autowired
	private ClienteDao clienteDao;
	
	@GetMapping({ "", "/" })
	public String menu(Model model) {
		model.addAttribute("titulo", "Administrador");
		return "catalogo/administrador/menuAdministrador";
	}
	
	@GetMapping({ "/clientes" })
	public String mostrarTodos(Model model) {
		model.addAttribute("titulo", "Administrador");
		model.addAttribute("clientes", clienteDao.findAll());
		return "catalogo/administrador/lista";
	}
	
	@GetMapping({ "/form" })
	public String form(Model model) {
		model.addAttribute("titulo", "cliente");
		Cliente nuevo = new Cliente();
		model.addAttribute("cliente", nuevo);
		return "catalogo/administrador/form";
	}
	
	@GetMapping({ "/form/{id}" })
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("titulo", "Cliente");
		Cliente editar = clienteDao.find(id);
		model.addAttribute("cliente", editar);
		return "catalogo/administrador/form";
	}
	
	@PostMapping({ "/guardar" })
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus sesion) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Cliente");
			return "catalogo/administrador/form";
		}
		System.out.println(cliente.getId());
		if (cliente.getId() != null && cliente.getId() > 0) {
			clienteDao.update(cliente);
		} else {
			clienteDao.insert(cliente);
		}

		sesion.setComplete(); 
		return "redirect:/administrador/clientes";
	}

	@GetMapping({ "/eliminar/{id}" })
	public String eliminar(@PathVariable Long id, Model model) {
		if (id != null && id > 0) {
			clienteDao.delete(id);
		}
		return "redirect:/administrador/clientes";
	}
	
}
