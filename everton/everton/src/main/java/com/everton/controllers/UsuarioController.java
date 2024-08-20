package com.everton.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.everton.dtos.UsuarioDto;
import com.everton.models.UsuarioModel;
import com.everton.repositories.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	UsuarioRepository repository;
	
	@GetMapping("/")
	public String index() {
		return "usuario/index";
	}
	
	@GetMapping("/inserir/")
	public String inserir() {
		return "usuario/inserir";
	}
	
	@PostMapping("/inserir/")
	public String inserirBD(@ModelAttribute @Valid UsuarioDto usuarioDto, BindingResult result, RedirectAttributes msg) {
		
		if(result.hasErrors()) {
			
			msg.addFlashAttribute("erroCadastrar","Erro ao cadastrar novo usuario");
			
			return "redirect:/usuario/inserir/";
			
		}
		var usuario = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuario);
		usuario.setTipo("comum");
		repository.save(usuario);
		
		msg.addFlashAttribute("sucessoCadastrar","Usuario cadastrado com sucesso!");
		
		return "redirect:/usuario/";
	}
	
	
	
	
	
	
	
	
}
