package com.bolsadeideas.sringboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.sringboot.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura,Long>{
	// Java Persistence Query Language
	@Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id=?1")
	public Factura fetchByIdWithClienteWhithItemFacturaWithProducto(Long id);

}
