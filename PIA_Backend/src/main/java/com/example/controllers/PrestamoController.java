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
import com.example.models.dao.PrestamoDao;
import com.example.models.entitys.Cliente;
import com.example.models.entitys.Prestamo;

@Controller
@RequestMapping(path = "/prestamo")
@SessionAttributes("prestamo")
public class PrestamoController {

	@Autowired
	private PrestamoDao prestamoDao;

	@GetMapping({ "", "/" })
	public String prestamos(Model model) {
		List<Prestamo> p = prestamoDao.findAll();
		for(Prestamo l : p) {
			System.out.println("Entra");
		System.out.println(l.getMonto());
		}
		model.addAttribute("titulo", "Prestamos");
		model.addAttribute("prestamos", p);
		return "catalogo/prestamo/lista";
	}

	@GetMapping({ "/form" })
	public String form(Model model) {
		model.addAttribute("titulo", "Prestamo");
		Prestamo nuevo = new Prestamo();
		model.addAttribute("prestamo", nuevo);
		return "catalogo/prestamo/form";
	}

	@GetMapping({ "/form/{id}" })
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("titulo", "Prestamo");
		Prestamo editar = prestamoDao.find(id);
		model.addAttribute("prestamo", editar);
		return "catalogo/prestamo/form";
	}

	@PostMapping({ "/guardar" })
	public String guardar(@Valid Prestamo prestamo, BindingResult result, Model model, SessionStatus sesion) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Prestamo");
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

}
