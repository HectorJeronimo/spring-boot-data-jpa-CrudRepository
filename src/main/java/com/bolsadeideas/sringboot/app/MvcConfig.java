package com.bolsadeideas.sringboot.app;

import org.springframework.context.annotation.Bean;

//import java.nio.file.Paths;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter; es deprecated en spring boot 2

/*Nota:
 * en Spring Boot 2 cambiar la herencia por la implementacion
 * public class MvcConfig implements WebMvcConfigurerAdapter
 * */
//Marcar la clase como una clase de configuracion
@Configuration
public class MvcConfig implements WebMvcConfigurer{
//public class MvcConfig extends WebMvcConfigurerAdapter{ es deprecated en spring boot 2

	//private final Logger log = LoggerFactory.getLogger(getClass());
	
	/* Este metodo lo que hace es habilitar un directorio como recurso del sistema
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		super.addResourceHandlers(registry);
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath);
		//.addResourceLocations("file:/C:/Users/hecto/Desktop/Mis Documentos compu trabajo/Spring Course/Temp/uploads/");
	}*/
	
	//Establece las vistas de errores
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
		
	}
	
	//Passwor encoder paso 1
	@Bean //Otra forma de registrar beans en el contenedor de spring como @componet_> esta se usa cuando se crea una clase de nosotros una clase personalizada
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	}
