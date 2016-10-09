package com.andmore.parkitmobile.entity;

import java.io.Serializable;

 

public class Seccion implements Serializable {
	
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 6731193176738342893L;
	private int id;
	private String nombre;
	private int centro_comercial_id;
	//private List<Nodo> nodo;
	//private List<Local> local;
	
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
	public int getCentro_comercial_id() {
		return centro_comercial_id;
	}
	public void setCentro_comercial_id(int centro_comercial_id) {
		this.centro_comercial_id = centro_comercial_id;
	}


}
