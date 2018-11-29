package com.bolsadeideas.sringboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.sringboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{
	
	//opcion 1 JPA  Java Persistence Query Language
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);
	
	//opcion 2 SPRING
	//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
	//Podemos generar las consultas con el nombre del metodo
	public List<Producto> findByNombreLikeIgnoreCase(String term);
	
}
