package com.bolsadeideas.sringboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.sringboot.app.models.entity.Cliente;

//https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
// antes extendiamos de CrudRepository<Cliente,Long>
//PagingAndSortingRepository extiende de CrudRepository
public interface IClienteDao extends PagingAndSortingRepository<Cliente,Long>{
	// Java Persistence Query Language
	
	@Query("select c from Cliente c left join fetch c.facturas f where c.id =?1")
	public Cliente fetchByIdWithFacturas(Long id);
	
	
	//se heredan los siguientes metodos de la clase CrudRepository
	//findAll()
	//count()
	//delete()
	//deleteAll()
	//save()
	//... y mas
	
	
	//query customizable
	//Example Declare query at the query method using @Query
	/*@Query("SELECT u FROM User u WHERE u.emailAddress = ?1")
	User findByEmailAddress(String email);*/
	
	// tambien podemos heredar de JpaRepository que ademas del crud implementa otras cosas
	//
	
	//al heredar de PagingAndSortingRepository tenemos que implementar dos metodos de diferente manera que en crudrepository
	//   - Iterable<T> findAll(Sort sort);
	//	 - Page<T> findAll(Pageable pageable);
	/*pageable tiene el atributo page y size
	 * page es la pagina actual y size el tamaño*/
	
}
