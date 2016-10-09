package com.andmore.parkitmobile.entity;

import java.io.Serializable;
 
public class Local implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6213538500323092720L;
	private int id;
	private String numero;
	private String nombre;
	private String descripcion;
	private String foto;
	private String categoria;
	private int centro_comercial_id;
	private int seccion;
	//private Seccion seccion;
	private String telefono;
	private String url;
	
	 
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	 
	public int getCentro_comercial_id() {
		return centro_comercial_id;
	}
	public void setCentro_comercial_id(int centro_comercial_id) {
		this.centro_comercial_id = centro_comercial_id;
	}
	 
	 	 
	public int getSeccion() {
		return seccion;
	}
	public void setSeccion(int seccion) {
		this.seccion = seccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	 
	
	 
	
	 
	
	 
	
	
}
