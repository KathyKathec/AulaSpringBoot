package com.everton.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		
		return "redirect:../";
	}
	
	
	@GetMapping("/listar/")
	public ModelAndView listar() {
		
		ModelAndView mv = new ModelAndView("usuario/listar");
		List<UsuarioModel> usuarios = repository.findAll();
		mv.addObject("usuarios", usuarios);
		return mv;		
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable (value="id") int id ) {
		ModelAndView mv = new ModelAndView("usuario/editar");
	
		Optional<UsuarioModel> usuario= repository.findById(id); 
		mv.addObject("id", usuario.get().getId());
		mv.addObject("email", usuario.get().getEmail());
		mv.addObject("nome", usuario.get().getNome());
		mv.addObject("tipo", usuario.get().getTipo());
		
		return mv;
		
		
		
	}
	
	@PostMapping("/editar/{id}")
	public String editarBD(@ModelAttribute @Valid UsuarioDto usuarioDto, BindingResult result,
			RedirectAttributes msg, @PathVariable(value="id") int id) {
		
		
		Optional<UsuarioModel> usuario= repository.findById(id); 
		
		
		if(result.hasErrors()) {
			
			msg.addFlashAttribute("erroEditar","Erro ao editar o usuario");
			
			return "redirect:/usuario/listar2/";
			
		}
		var usuarioModel = usuario.get();
		BeanUtils.copyProperties(usuarioDto, usuarioModel);
		repository.save(usuarioModel);
		
		msg.addFlashAttribute("sucessoEditar","Usuario editado com sucesso!");
		
		return "redirect:../../usuario/listar/";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value="id")int id) {
		Optional<UsuarioModel> usuario = repository.findById(id);
		if(usuario.isEmpty()) {
			return "redirect:../../usuario/listar/";
		}
		repository.deleteById(id);
		return "redirect:../../usuario/listar/";
		
	}
	
	
	
}
