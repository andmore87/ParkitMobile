package com.andmore.parkitmobile.entity;

import java.io.Serializable;

 

public class Nodo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3003246250560259684L;
	private int id;
	private String nombre;
	private int seccion_id;
	private int estado;
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
	 
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getSeccion_id() {
		return seccion_id;
	}
	public void setSeccion_id(int seccion_id) {
		this.seccion_id = seccion_id;
	}
	
	
	
	

}
