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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.models.dao.ClienteDao;
import com.example.models.dao.PrestamoDao;
import com.example.models.dao.UsuarioActualDaoImp;
import com.example.models.entitys.Cliente;

@Controller
@RequestMapping(path = "/cliente")
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private ClienteDao clienteDao;
	
	@Autowired
	private PrestamoDao prestamoDao;
	
	@Autowired
	private UsuarioActualDaoImp usAct;
	
	@GetMapping({"/menu" })
	public String menu(Model model) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		model.addAttribute("titulo", "Cliente");
		model.addAttribute("id", usAct.get());
		model.addAttribute("clientes", clienteDao.findAll());
		model.addAttribute("prestamos", prestamoDao.findIdCliente(usAct.get()));
		return "catalogo/cliente/menuCliente";
	}

	@GetMapping({ "", "/" })
	public String clientes(Model model) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		model.addAttribute("titulo", "Cliente");
		model.addAttribute("id", usAct.get());
		model.addAttribute("clientes", clienteDao.findAll());
		model.addAttribute("prestamos", prestamoDao.findIdCliente(usAct.get()));
		return "catalogo/cliente/menuCliente";
	}
	
	@GetMapping({ "/abono/{id}" })
	public String abonar(@PathVariable Long id,Model model) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		Cliente editar = clienteDao.find(id);
		model.addAttribute("cliente", editar);
		return "catalogo/cliente/abono";
	}
	
	@GetMapping({ "/retiro/{id}" })
	public String retirar(@PathVariable Long id,Model model) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		Cliente editar = clienteDao.find(id);
		model.addAttribute("cliente", editar);
		return "catalogo/cliente/retiro";
	}
	
	@GetMapping({ "/cambiarMonto" })
	public String abonar(@Valid Cliente cliente,@RequestParam Integer action,@RequestParam float cantidad,Model model,SessionStatus sesion) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		if(action==0) {
			cliente.setMonto(cliente.getMonto()+cantidad);
		}else{
			if(cantidad>=cliente.getMonto()) {
				System.out.println("Case cantidad>monto del cliente");
				cliente.setMonto(0.0f);
			}else {
				cliente.setMonto(cliente.getMonto()-cantidad);
			}
		}
		clienteDao.update(cliente);
		System.out.println(cantidad);
		sesion.setComplete();
		return "redirect:/cliente";
	}

	@GetMapping({ "/form" })
	public String form(Model model) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		model.addAttribute("titulo", "cliente");
		Cliente nuevo = new Cliente();
		model.addAttribute("cliente", nuevo);
		return "catalogo/cliente/form";
	}

	@GetMapping({ "/form/{id}" })
	public String editar(@PathVariable Long id, Model model) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		model.addAttribute("titulo", "Cliente");
		Cliente editar = clienteDao.find(id);
		model.addAttribute("cliente", editar);
		return "catalogo/cliente/editarCliente";
	}

	@PostMapping({ "/guardar" })
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus sesion) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Cliente");
			return "catalogo/cliente/form";
		}
		System.out.println(cliente.getId());
		if (cliente.getId() != null && cliente.getId() > 0) {
			clienteDao.update(cliente);
		} else {
			clienteDao.insert(cliente);
		}
		return "redirect:/cliente";
	}

	@GetMapping({ "/eliminar/{id}" })
	public String eliminar(@PathVariable Long id, Model model) {
		if(!usAct.estaConectado()) {
			return "redirect:/login";
		}
		if(usAct.esAdmin()) {
			return "redirect:/administrador";
		}
		if (id != null && id > 0) {
			clienteDao.delete(id);
		}
		return "redirect:/cliente";
	}

}
