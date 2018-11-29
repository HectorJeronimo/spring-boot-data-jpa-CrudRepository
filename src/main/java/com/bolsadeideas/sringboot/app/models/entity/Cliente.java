package com.bolsadeideas.sringboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;// son deprecadas en spring boot 2
import javax.validation.constraints.NotEmpty;// son deprecadas en spring boot 2
//import org.hibernate.validator.constraints.Email;// son deprecadas en spring boot 2
//import org.hibernate.validator.constraints.NotEmpty;// son deprecadas en spring boot 2
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.OneToMany;
//import javax.persistence.PrePersist;

@Entity // declarar clase de persistencia o entidad
@Table(name = "clientes") // Esta etiqueta se pudiera omitir si el nombre de la clase se llamara igual que
							// la tabla en la base de datos
public class Cliente implements Serializable {

	/**
	 * agrgar el serial version id
	 */
	private static final long serialVersionUID = 1L;

	@Id // indica que es una llave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // es una llave autoincremental
	private Long id;

	@NotEmpty // en spring boot 2 la anotacion del packages
				// org.hibernate.validator.constraints es deprecated
	// javax.validation.constrains es el package bueno en spring 2
	private String nombre;

	@NotEmpty
	private String apellido;

	@NotEmpty
	@Email // lo mismo sucede con esta etiqueta packages
			// org.hibernate.validator.constraints es deprecated
	private String email;

	@NotNull
	@Column(name = "create_at") // nos permite agregar otros atributos como si es insertable,el largo de la
								// columna etc van separados por coma
	@Temporal(TemporalType.DATE) // convertir y formatiar la fecha de la base de datos
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;
	
	//mappedby es como se llama el objeto en la otra Entity 
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY,cascade=CascadeType.ALL)//EAGER o LAZY cargar los datos es la forma mas recomendada la LAZY porque EAGER carga mas de datos
	//cascade=CascadeType.ALL indica que todas las operaciones update y delete se haran en cadena. si el cliente se elimina se eliminan las facturas.
	private List<Factura> facturas;

	private String foto;

	/*
	 * //Para que funcione este metodo y se ejecute como un evento
	 * 
	 * @PrePersist public void prePersist() { createAt = new Date(); }
	 */
	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public void addFactura(Factura factura) {
		facturas.add(factura);
	}

	@Override
	public String toString() {
		return  nombre + " " + apellido;
	}
	
	

}
