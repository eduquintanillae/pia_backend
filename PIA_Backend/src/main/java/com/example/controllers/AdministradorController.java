package com.example.controllers;

import java.util.List;

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
import com.example.services.interfaces.DineroInterface;

@Controller
@RequestMapping(path = "/administrador")
@SessionAttributes("cliente")
public class AdministradorController {

	@Autowired
	private ClienteDao clienteDao;
	
	@Autowired
	private DineroInterface dinero;
	
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
	
	@GetMapping({ "/buqueda/clientes" })
	public String busquedaTodos(Model model) {
		model.addAttribute("titulo", "Lista de clientes");
		model.addAttribute("clientes", clienteDao.findAll());
		return "catalogo/administrador/busqueda/lista";
	}
	
	@GetMapping({ "/buqueda/id" })
	public String busquedaID(Model model) {
		model.addAttribute("titulo", "Busqueda por ID");
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("mensaje", "");
		return "catalogo/administrador/busqueda/busquedaID";
	}
	
	@PostMapping({ "/buqueda/id/result" })
	public String buscadorid(Long id, Model model) {
		Cliente c = clienteDao.findId(id);
		model.addAttribute("titulo", "Busqueda por ID");
		model.addAttribute("cliente", c);
		if(c.getId() != null)
			model.addAttribute("mensaje", "Cliente encontrado");
		else 
			model.addAttribute("mensaje", "Cliente no encontrado");
		return "catalogo/administrador/busqueda/busquedaID";
	}
	
	@GetMapping({ "/buqueda/nombre" })
	public String busquedaNombre(Model model) {
		model.addAttribute("titulo", "Busqueda por Nombre");
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("mensaje", "");
		return "catalogo/administrador/busqueda/busquedaNombre";
	}
	
	@PostMapping({ "/buqueda/nombre/result" })
	public String buscadorNombre(String nombre, Model model) {
		System.out.println(nombre);
		List<Cliente> lista = clienteDao.findNombre(nombre);
		if (nombre != null && nombre != "") {
			model.addAttribute("titulo", "Busqueda por Nombre");
			model.addAttribute("clientes", lista);
			model.addAttribute("mensaje", "Cliente encontrado");
			return "catalogo/administrador/busqueda/busquedaNombre";
		}
		model.addAttribute("titulo", "Busqueda por Nombre");
		model.addAttribute("clientes", new Cliente());
		model.addAttribute("mensaje", "Cliente no encontrado");
		return "catalogo/administrador/busqueda/busquedaNombre";
	}
	
	@GetMapping({ "/monto" })
	public String mostrarMontoTotal(Model model) {
		model.addAttribute("titulo", "Monto total");
		model.addAttribute("monto", dinero.montoTotal());
		model.addAttribute("clientes", clienteDao.findAll());
		return "catalogo/administrador/montoTotal";
	}
	
	@GetMapping({ "/montomayor" })
	public String mostrarMontoMayor(Model model) {
		model.addAttribute("titulo", "Cliente con más dinero");
		model.addAttribute("cliente", dinero.clienteMasAdinerado());
		return "catalogo/administrador/clienteMontoMayor";
	}
	
}
