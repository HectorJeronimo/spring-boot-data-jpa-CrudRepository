package com.bolsadeideas.sringboot.app.models.service;

import java.util.ArrayList;
///import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.sringboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.sringboot.app.models.entity.Role;
import com.bolsadeideas.sringboot.app.models.entity.Usuario;

@Service("jpaUserDetailsService")//De esta forma se registra como un componente de spring.es un estereotipo de component
//UserDetailsService es una interface propia de Spring security para trabajar con JPA o cualquier tipo de provedor para el proceso de autentificacion
public class JpaUserDetailsService implements UserDetailsService{
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDao.findByUsername(username);
		
		if(usuario == null) {
			logger.error("Error login: no existe el usuario '"+username+"'");
			throw new UsernameNotFoundException("Username "+username+" no existe en el sistema!");
		}
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Role role: usuario.getRoles()) {
			logger.info("Role: "+role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		if(authorities.isEmpty()) {
			logger.error("Error login: usuario '"+username+"' no tiene roles asignados");
			throw new UsernameNotFoundException("Username "+username+" no tiene roles asignados en el sistema!");
		}
		
		
		return new User(usuario.getUsername(), 
				usuario.getPassword(),
				usuario.getEnabled(),
				true,
				true,
				true,
				authorities);
	}
	
	

}
