package com.andmore.parkitmobile.entity;

import java.io.Serializable;
import java.util.List;

   
public class Centro_Comercial implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2167104596974447373L;
	//seam-gen attributes (you should probably edit these)
	private int id;
	private String nombre;
	private String direccion;
	private String horario;
	private String telefono;
	private String foto;
	private String web;
	private String facebook;
	private String twitter;
	private String instagram;
	private String precio;
	private List<Seccion> seccion;
	
    		
	
	public int getId() {
	     return id;
	}

	public void setId(int id) {
	     this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public List<Seccion> getSeccion() {
		return seccion;
	}

	public void setSeccion(List<Seccion> seccion) {
		this.seccion = seccion;
	}

 	
}
