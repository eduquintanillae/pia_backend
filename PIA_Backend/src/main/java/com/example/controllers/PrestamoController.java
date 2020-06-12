package com.example.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.models.dao.ClienteDao;
import com.example.models.dao.PrestamoDao;
import com.example.models.entitys.Cliente;
import com.example.models.entitys.Prestamo;
import com.example.services.interfaces.DineroInterface;

@Controller
@RequestMapping(path = "/prestamo")
@SessionAttributes("prestamo")
public class PrestamoController {
	@Autowired
	private ClienteDao clienteDao;	
	
	@Autowired
	private PrestamoDao prestamoDao;
	
	@Autowired
	private DineroInterface dinero;

	@GetMapping({ "/cliente/form/{id}" })
	public String clienteForm(@PathVariable Long id, Model model) {
		model.addAttribute("titulo", "Solicitud de prestamo");
		Cliente c = clienteDao.find(id);
		Prestamo nuevo = new Prestamo();
		nuevo.setCliente(c);
		nuevo.setPagado(false);
		nuevo.setAbonoTotal((float) 0.0);
		model.addAttribute("prestamo", nuevo);
		return "catalogo/prestamo/cliente/form";
	}
	
	
	@PostMapping({ "/guardarCliente" })
	public String guardarCliente(@Valid Prestamo prestamo, BindingResult result, Model model, SessionStatus sesion) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Prestamo");
			return "catalogo/prestamo/cliente/form";
		}
		System.out.println(prestamo.getId());
		if (prestamo.getId() != null && prestamo.getId() > 0) {
			prestamoDao.update(prestamo);
		} else {
			prestamoDao.insert(prestamo);
		}
		sesion.setComplete();
		return "redirect:/prestamo";
	}
	
	@GetMapping({ "/cliente/abono/{id}" })
	public String abonarPrestamoCliente(@PathVariable Long id,Model model) {
		model.addAttribute("titulo", "Abonar");
		Prestamo prestamo_a=prestamoDao.find(id);
		model.addAttribute("prestamo", prestamo_a);
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/cliente/abono";
	}
	
	@GetMapping({ "/cliente/abonoCN/{id}" })
	public String abonarPrestamoClienteCN(@PathVariable Long id,Model model) {
		model.addAttribute("titulo", "Abonar");
		Prestamo prestamo_a=prestamoDao.find(id);
		model.addAttribute("prestamo", prestamo_a);
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/cliente/abonoCN";
	}
	
	@PostMapping({ "/cliente/abono/resultado" })
	public String abonadoPrestamoCliente(Prestamo prestamo, Model model,SessionStatus sesion) {
		System.out.println("ID del prestamo: "+prestamo.getId()+" Monto abonado:"+prestamo.getMonto());
		if(prestamo.getId() != null && prestamo.getMonto() != null) {
			System.out.println("entre");
			if(dinero.abonarCliente(prestamo.getId(), prestamo.getMonto())) {
				model.addAttribute("prestamo", prestamoDao.find(prestamo.getId()));
				model.addAttribute("mensaje", "Abono realizado, monto aun insuficiente");
			}
			else {
				model.addAttribute("prestamo", new Prestamo());
				model.addAttribute("mensaje", "Monto total cubierto");
			}
			model.addAttribute("titulo", "Abonar");
			return "catalogo/prestamo/cliente/abono";
		}
		model.addAttribute("titulo", "Abonar");
		model.addAttribute("prestamo", new Prestamo());
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/cliente/abono";
	}
	
	@PostMapping({ "/cliente/abono/resultadoCN" })
	public String abonadoPrestamoClienteCN(Prestamo prestamo, Model model,SessionStatus sesion) {
		System.out.println("ID del prestamo: "+prestamo.getId()+" Monto abonado:"+prestamo.getMonto());
		if(prestamo.getId() != null && prestamo.getMonto() != null) {
			System.out.println("entre");
			if(dinero.abonarClienteCN(prestamo.getId(), prestamo.getMonto())) {
				model.addAttribute("prestamo", prestamoDao.find(prestamo.getId()));
				model.addAttribute("mensaje", "Abono realizado, monto aun insuficiente");
			}
			else {
				model.addAttribute("prestamo", new Prestamo());
				model.addAttribute("mensaje", "Monto total cubierto");
			}
			model.addAttribute("titulo", "Abonar");
			return "catalogo/prestamo/cliente/abono";
		}
		model.addAttribute("titulo", "Abonar");
		model.addAttribute("prestamo", new Prestamo());
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/cliente/abono";
	}


	@GetMapping({ "", "/" })
	public String prestamos(Model model) {
		System.out.println("correcto");
		List<Prestamo> p = prestamoDao.findOrdenAscendete();
		for(Prestamo l : p) {
			System.out.println("Entra");
			if(l.getCliente() != null)
				System.out.println(l.getCliente().getNombre());
		}
		model.addAttribute("titulo", "Prestamos");
		model.addAttribute("prestamos", p);
		return "catalogo/prestamo/lista";
	}

	@GetMapping({ "/form" })
	public String form(Model model) {
		
		model.addAttribute("lista_clientes", clienteDao.findAll());
		
		
		model.addAttribute("titulo", "Prestamo");
		Prestamo nuevo = new Prestamo();
		model.addAttribute("prestamo", nuevo);
		return "catalogo/prestamo/form";
	}

	@GetMapping({ "/form/{id}" })
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("lista_clientes", clienteDao.findAll());
		model.addAttribute("titulo", "Editar Prestamo");
		Prestamo editar = prestamoDao.find(id);
		model.addAttribute("prestamo", editar);
		return "catalogo/prestamo/formEditar";
	}

	@PostMapping({ "/guardar" })
	public String guardar(@Valid Prestamo prestamo, BindingResult result, Model model, SessionStatus sesion) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Prestamo");
			model.addAttribute("lista_clientes", clienteDao.findAll());
			return "catalogo/prestamo/form";
		}
		System.out.println(prestamo.getId());
		if (prestamo.getId() != null && prestamo.getId() > 0) {
			prestamoDao.update(prestamo);
		} else {
			prestamoDao.insert(prestamo);
		}

		sesion.setComplete();
		return "redirect:/prestamo";
	}

	@GetMapping({ "/eliminar/{id}" })
	public String eliminar(@PathVariable Long id, Model model) {
		if (id != null && id > 0) {
			prestamoDao.delete(id);
		}
		return "redirect:/prestamo";
	}
	
	@GetMapping({ "/buscar/prestamos" })
	public String buscarPrestamos(Model model) {
		List<Prestamo> p = prestamoDao.findAll();
		model.addAttribute("titulo", "Prestamos");
		model.addAttribute("prestamos", p);
		return "catalogo/prestamo/busqueda/lista";
	}
	
	@GetMapping({ "/buscar/prestamos/id" })
	public String buscarPrestamosId(Long id, Model model) {
		model.addAttribute("titulo", "Prestamos");
		model.addAttribute("prestamo", new Prestamo());
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/busqueda/busquedaId";
	}
	
	@PostMapping({ "/buscar/prestamos/id/result" })
	public String buscdorPrestamosId(Long id, Model model) {
		List<Prestamo> p = prestamoDao.findIdCliente(id);
		model.addAttribute("titulo", "Prestamos");
		model.addAttribute("prestamos", p);
		if(p.isEmpty())
			model.addAttribute("mensaje", "Prestamo no encontrado");
		else
			model.addAttribute("mensaje", "Prestamo encontrado");
		return "catalogo/prestamo/busqueda/busquedaId";
	}
	
	@GetMapping({ "/buscar/prestamos/fecha" })
	public String buscarPrestamosFecha(Long id, Model model) {
		model.addAttribute("titulo", "Prestamos");
		model.addAttribute("prestamo", new Prestamo());
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/busqueda/busquedaFecha";
	}
	
	@GetMapping(path = "/buscar/prestamos/fecha/result")
	public String buscdorPrestamosFecha(@RequestParam String fechaCreacion, @RequestParam String fechaExpiracion, Model model) {
		List<Prestamo> p = new ArrayList<Prestamo>();
		System.out.println(fechaCreacion);
		System.out.println(fechaExpiracion);
		if(fechaCreacion != null && fechaExpiracion != null) {
			try {
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(fechaCreacion);  
				Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(fechaExpiracion);  
				p = prestamoDao.findFecha(date1, date2);
				if(p.isEmpty())
					model.addAttribute("mensaje", "Prestamos no encontrados entre ".concat(date1.toString()).concat(" y ").concat(date2.toString()));
				else
					model.addAttribute("mensaje", "Prestamos encontrados entre ".concat(date1.toString()).concat(" y ").concat(date2.toString()));
			}
			catch(Exception e) {
				model.addAttribute("mensaje", "Fechas no validas favor de seguir el formato dd/MM/yyyy");
			}
		}
		model.addAttribute("titulo", "Prestamos");
		model.addAttribute("prestamos", p);
		return "catalogo/prestamo/busqueda/busquedaFecha";
	}

	@GetMapping({ "/buscar/prestamos/activos" })
	public String buscarPrestamosActivos(Model model) {
		List<Prestamo> p = prestamoDao.findActivos();
		model.addAttribute("titulo", "Prestamos activos");
		model.addAttribute("prestamos", p);
		return "catalogo/prestamo/busqueda/lista";
	}

	@GetMapping({ "/buscar/prestamos/pagados" })
	public String buscarPrestamosPagados(Model model) {
		List<Prestamo> p = prestamoDao.findPagados();
		model.addAttribute("titulo", "Prestamos pagados");
		model.addAttribute("prestamos", p);
		return "catalogo/prestamo/busqueda/lista";
	}
	
	@GetMapping({ "/abono" })
	public String abonarPrestamo(Model model) {
		model.addAttribute("titulo", "Abonar");
		model.addAttribute("prestamo", new Prestamo());
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/abono";
	}
	
	@PostMapping({ "/abono/resultado" })
	public String abonadoPrestamo(Prestamo prestamo, Model model) {
		if(prestamo.getId() != null && prestamo.getMonto() != null) {
			System.out.println("entre");
			if(dinero.abonar(prestamo.getId(), prestamo.getMonto())) {
				model.addAttribute("prestamo", prestamoDao.find(prestamo.getId()));
				model.addAttribute("mensaje", "Prestamo abonado");
			}
			else {
				model.addAttribute("prestamo", new Prestamo());
				model.addAttribute("mensaje", "Monto del cliente es insuficiente");
			}
			model.addAttribute("titulo", "Abonar");
			return "catalogo/prestamo/abono";
		}
		model.addAttribute("titulo", "Abonar");
		model.addAttribute("prestamo", new Prestamo());
		model.addAttribute("mensaje", "");
		return "catalogo/prestamo/abono";
	}
	
}
