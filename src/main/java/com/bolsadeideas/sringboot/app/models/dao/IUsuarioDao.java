package com.bolsadeideas.sringboot.app.models.dao;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.sringboot.app.models.entity.Usuario;


//se puede heredar de CrudRepository y de JpaRepository org.springframework.data.jpa.repository.JpaRepository
public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	//a traves del nombre del metodo (Query method name) se ejecuta la consulta JPQL
	//select u from Usuario u where u.username=?1
	public Usuario findByUsername(String username);
	
	/*//opc 2
	@Query("select u from Usuario u where u.username=?1")
	public Usuario getUser(String username);*/
}
