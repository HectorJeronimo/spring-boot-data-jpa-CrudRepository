package com.bolsadeideas.sringboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	
	@GetMapping("/login")//importante habilitar esta ruta en el SpringSecurityConfig
	public String login(@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required=false) String logout,
			Model model, Principal principal, RedirectAttributes flash) {
		
		/*El objeto principal nos permite validar si un usuari habia iniciado session anteriormente*/
		if(principal != null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
			return "redirect:/";
		}
		
		if(error != null) {
			model.addAttribute("error","Error en el login: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}
		
		if(logout != null) {
			model.addAttribute("success","Ha cerrado sesion con éxito");
		}
		
		return "login";
	}

}
