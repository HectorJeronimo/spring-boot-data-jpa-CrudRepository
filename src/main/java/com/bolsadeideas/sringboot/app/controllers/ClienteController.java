package com.bolsadeideas.sringboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.sringboot.app.models.entity.Cliente;
import com.bolsadeideas.sringboot.app.models.service.IClienteService;
import com.bolsadeideas.sringboot.app.models.service.IUploadFileService;
import com.bolsadeideas.sringboot.app.util.paginator.PageRender;

@Controller
//es mas seguro el sseionattributes que utilizar el campo hidden, es una practica mas recomendada
@SessionAttributes("cliente") //se indica que se va a guardar el objeto cliente en los atributos de la sesion
public class ClienteController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	//en la clase ClienteDaoImpl se pone la etiqueta @Repository("clienteDaoJPA") con el nombre del bean y ese se usa en la etiqueta @Qualifier para hacer referencia y evitar ambiguedad
	//@Qualifier("clienteDaoJPA")//es para elejir el bean correcto.esto evita la ambiguedad ya que varias clases pueden implementar la misma interface y habria varios en el repository
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	
	
	/*Esta es otra forma que 
	 * Sustituye a la forma de acceder a la imagen en el folder del proyecto
	 * como se hace en el metodo comentado en la clase MvcConfig.java
	 * 
	 * Este metodo obtiene la imagen y la carga en una peticion HTTP
	 * (Cargando imagen programaticamente en la respuesta HTTP con ResponseEntity)
	 * */
	
	
	@Secured("ROLE_USER")
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"")
				.body(recurso);
	}
	
	
	@Secured("ROLE_USER")
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String,Object> model,RedirectAttributes flash) {
		
		//Cliente cliente = clienteService.findOne(id);//hace varias consultas
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);//consulta optimizada
		
		if(cliente==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo","Detalle cliente: "+cliente.getNombre());
		
		return "ver";
	}
	
	//@Secured("ROLE_USER") ESTE METODO ES PUBLICO
	//RequestMethod GET,POST,PUT,TRACE,DELETE etc por defecto es GET
	//{"/","/listar"} este es un arreglo que son dos rutas que representa lo mismo
	@RequestMapping(value=  {"/","/listar"},method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0")int page, Model model,
			Authentication authentication,
			HttpServletRequest request) {
		
		/*pasando por argumento el objeto Authentication*/
		if(authentication != null) {
			logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
		}
		
		/*Hacerlo de la manera estatica y obtener el objeto Authentication: de esta manera se puede usar 
		 * este objeto en toda la aplcacion en cualquier parte*/
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			logger.info("Utilizando forma estatica  SecurityContextHolder.getContext().getAuthentication():  Hola usuario autenticado, tu username es: ".concat(auth.getName()));
		}
		
		/*VALIDAR ROL OPCION 1 CREANDO LA FUNCION EN ESTE CONTROLADOR hasRole() "FORMA MANUAL" se tiene mas control*/
		if(hasRole("ROLE_ADMIN")) {
			logger.info(  "FORMA 1:Hola ".concat(auth.getName()).concat(" tienes acceso!")  );
		}else {
			logger.info(  "FORMA 1:Hola ".concat(auth.getName()).concat(" NO tienes acceso!")  );
		}
		
		/*VALIDAR ROL OPCION 2 con SecurityContextHolderAwareRequestWrapper*/
		SecurityContextHolderAwareRequestWrapper securityContext = 
				new SecurityContextHolderAwareRequestWrapper(request,"");
		//Notese el string del rol se le pasa una parte al new SecurityContextHolderAwareRequestWrapper(request,"ROLE_")
		//y otra parte al isUserInRole("ADMIN")
		if(securityContext.isUserInRole("ROLE_ADMIN")) {
			logger.info(  "FORMA 2:Hola ".concat(auth.getName()).concat(" tienes acceso!")  );
		}else {
			logger.info(  "FORMA 2:Hola ".concat(auth.getName()).concat(" NO tienes acceso!")  );
		}
		
		/*VALIDAR ROL OPCION 3 con HttpServletRequest*/
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info(  "FORMA 3:Hola ".concat(auth.getName()).concat(" tienes acceso!")  );
		}else {
			logger.info(  "FORMA 3:Hola ".concat(auth.getName()).concat(" NO tienes acceso!")  );
		}
		
		
		/*Nota en Spring Boot 2 El constructor es obsoleto en vez de esto se usa:
		 * PageRequest.of(page,size);*/
		int size = 5;
		//Pageable pageRequest = new PageRequest(page,size);es deprecated en spring boot 2
		Pageable pageRequest = PageRequest.of(page,size);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar",clientes);
		model.addAttribute("titulo","Listado de clientes");
		model.addAttribute("clientes",clientes);
		model.addAttribute("page",pageRender);
		return "listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form")//por defecto es get porque solo es para mostrar el formulario
	public String crear(Map<String,Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form",method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente,BindingResult result,Model model,@RequestParam("file")MultipartFile foto ,RedirectAttributes flash,SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		if(!foto.isEmpty()) {
			
			//Borrar la foto en una modificacion
			if(cliente.getId() != null
					&& cliente.getId() > 0
					&& cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				uploadFileService.delete(cliente.getFoto());
			}
			
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", "Has subido correctamente '"+uniqueFilename+"'");
			cliente.setFoto(uniqueFilename);
			
		}
		String mensajeFlash = (cliente.getId() != null? "Cliente editado con éxito!" : "Cliente creado con éxito");
		
		clienteService.save(cliente);
		status.setComplete();//va eliminar el objeto cliente de la session
		flash.addFlashAttribute("success",mensajeFlash);
		return "redirect:listar";
		
	}
	
	@Secured("ROLE_ADMIN")
	//wildcar pasar parametros por url 
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model,RedirectAttributes flash) {
		Cliente cliente = null;
		
		if(id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error","El ID del cliente no existe en la base de datos!");
			}
		}else {
			flash.addFlashAttribute("error","El ID del cliente no puede ser cero!");
			return"redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo","Editar Cliente");
		return "form";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if(id>0) {
			Cliente cliente = clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success","Cliente eliminado con éxito!");
			
			
			if(uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto "+cliente.getFoto()+" eliminada con exito!.");
			}
		}
		
		return "redirect:/listar";
	}
	
	
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
		//todo clase rol tiene que implementar esta interface GrantedAuthority
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*for(GrantedAuthority authority: authorities) {
			if(role.equals(authority.getAuthority())) {
				logger.info(  "Hola usuario".concat(auth.getName()).concat(" tu rol es ").concat(authority.getAuthority())  );
				return true;
			}
		}
		
		return false;*/
		
	}
	
	
}
