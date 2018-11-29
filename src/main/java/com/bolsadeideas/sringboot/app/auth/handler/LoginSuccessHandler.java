package com.bolsadeideas.sringboot.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		SessionFlashMapManager flashManager = new SessionFlashMapManager();
		FlashMap flashMap = new FlashMap();
		
		flashMap.put("success", "Hola,"+ authentication.getName() +"haz iniciado sesión con éxito!");
		
		flashManager.saveOutputFlashMap(flashMap,request,response);
		
		if(authentication != null) {
			//Notese que este es un atributo de una super clase que hereda SimpleUrlAuthenticationSuccessHandler
			//AbstractAuthenticationTargetUrlRequestHandler {
			//protected final Log logger = LogFactory.getLog(this.getClass());
			
			logger.info("El usuario ("+authentication.getName()+") ha iniciado sesión con éxito");
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
