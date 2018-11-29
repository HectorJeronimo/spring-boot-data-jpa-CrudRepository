package com.bolsadeideas.sringboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.sringboot.app.models.dao.IClienteDao;
import com.bolsadeideas.sringboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.sringboot.app.models.dao.IProductoDao;
import com.bolsadeideas.sringboot.app.models.entity.Cliente;
import com.bolsadeideas.sringboot.app.models.entity.Factura;
import com.bolsadeideas.sringboot.app.models.entity.Producto;

//Business Service Facade
@Service //esta clase esta basada en el patron de diseño Facade o Fachada
//un unico punto de acceso a distintos DAOS o repository
public class ClienteServiceImpl implements IClienteService {

	@Autowired//inyectar 
	private IClienteDao clienteDao;//aqui tenemos un dao
	//pero podemos tener mas daos y acceder a los demas daos en un unico punto de acceso
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired 
	private IFacturaDao facturaDao;
	
	
	//NOTA: en el Spring Boot 2 cambiaron algunos metodos en la interface 
	/*
	 * findOne(id) cambia por -> findById(id)
	 * delete(id) cambia por -> deleteById(id)
	 * 
	 * 
	 * Invocamos el metodo orElse() para retornar el objeto entity o bien null:
	 * return clienteDao.findById(id).orElse(null);
	 * */
	
	/*@Override
	@Transactional( readOnly=true )// va hagarrar el contenido del metodo y lo va envolver en una transaccion
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();//se puede el cast porque list es un collecton y a su vez collection hereda de iterable
	}*/
	@Override
	@Transactional( readOnly=true )// va hagarrar el contenido del metodo y lo va envolver en una transaccion
	public Page<Cliente> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return  clienteDao.findAll(pageable);
	}
	
	
	@Override
	@Transactional// sin el atributo readOnly ya que sera para insertar
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Override
	@Transactional( readOnly=true )
	public Cliente findOne(Long id) {
		//Esto retorna un Optional que es una nueva clase de java 8
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}


	@Override
	@Transactional( readOnly=true )
	public List<Producto> findByNombre(String term) {
		// TODO Auto-generated method stub
		//opcion 1
		//return productoDao.findByNombre(term);
		//opcion 2
		return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
	}


	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		// TODO Auto-generated method stub
		facturaDao.save(factura);
	}


	@Override
	@Transactional( readOnly=true )// los metodos del crudrepository son transaccionales por defecto
	//pero es buena practica anotarlos con @Transactional
	public Producto findProductoById(Long id) {
		return productoDao.findById(id).orElse(null);//en spring 5 el metodo findOne cambia a findById
	}


	@Override
	@Transactional( readOnly=true )
	public Factura findFacturaById(Long id) {

		return facturaDao.findById(id).orElse(null);
	}


	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);//en spring boot 2: facturaDao.deleteById(id);
	}


	@Override
	@Transactional( readOnly=true )
	public Factura fetchFacturaByIdWithClienteWhithItemFacturaWithProducto(Long id) {
		return facturaDao.fetchByIdWithClienteWhithItemFacturaWithProducto(id);
	}

	@Override
	@Transactional( readOnly=true )
	public Cliente fetchByIdWithFacturas(Long id) {
		return clienteDao.fetchByIdWithFacturas(id);
	}

	

}
