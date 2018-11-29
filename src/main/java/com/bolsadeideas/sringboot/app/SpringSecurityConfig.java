package com.bolsadeideas.sringboot.app;

//import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;*/
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.sringboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.sringboot.app.models.service.JpaUserDetailsService;

//OPCION 2 PARA DAR PERMISOS CONTROLADOR-ROL
/*1) @EnableGlobalMethodSecurity(securedEnabled=true,prePostEnabled=true)
 * prePostEnabled=true es para usar @PreAuthorize
 * 
 *2) @Secured("ROL_USER") en cada GetMapping o en cada metodo que responda a una url
 *  Si @Secured("ROL_USER") se pone antes de la clase quiere decir que todos sus metodos solo los podra ejecutar el rol
 *  especificado.
 *  
 *  Se pueden especificar mas roles de la siguiente manera
 *  @Secured({"ROL_USER","ROLE_ADMIN"})
 *  
 *  
 * 3) tambienn se usa @PreAuthorize de la siguiente mandera:
 *  @PreAuthorize("hasRole('ROL_USER')")
 *  (https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)
 *  
 *  
 *   Se pueden especificar mas roles de la siguiente manera
 *    @PreAuthorize("hasAnyRole('ROL_USER','ROLE_ADMIN')")
 *    
 *   hay mas expresiones para la autorizacion:
 *   	hasRole([role])	Returns true if the current principal has the specified role.
		hasAnyRole([role1,role2])	Returns true if the current principal has any of the supplied roles (given as a comma-separated list of strings)
		principal	Allows direct access to the principal object representing the current user
		authentication	Allows direct access to the current Authentication object obtained from the SecurityContext
		permitAll	Always evaluates to true
		denyAll	Always evaluates to false
		isAnonymous()	Returns true if the current principal is an anonymous user
		isRememberMe()	Returns true if the current principal is a remember-me user
		isAuthenticated()	Returns true if the user is not anonymous
		isFullyAuthenticated() Returns true if the user is not an anonymous or a remember-me user
 * */
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	//@Autowired
	//private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired 
	private JpaUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/listar","/css/**","/js/**","/images/**").permitAll()
		//OPCION 1 PARA DAR PERMISOS CONTROLADOR-ROL
		/*.antMatchers("/ver/**").hasAnyRole("USER")
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and()
		.formLogin()
			.successHandler(successHandler)
			.loginPage("/login").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		/*En la version final de Spring Boot 2 Release reemplazar la siguiente linea 
		 * UserBuilder users = User.withDefaultPasswordEncoder(); por
		 * PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(),
		 * UserBuilder users = User.builder().passwordEncoder(encoder::encoder); 
		 * */
		/*//Opcion 1 Sistema de autenticacion en memoria
		UserBuilder users = User.withDefaultPasswordEncoder();
		build.inMemoryAuthentication()
		.withUser(users.username("admin").password("12345").roles("ADMIN","USER"))
		.withUser(users.username("hector").password("12345").roles("USER"));*/
		
		//Opcion 2 Spring security con JDBC
		/*build.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("select username, password, enabled from users where username=?")
		.authoritiesByUsernameQuery("select u.username,a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");
		*/
		
		/*Opcion 3 */
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
		
		
		
		
	}
}
