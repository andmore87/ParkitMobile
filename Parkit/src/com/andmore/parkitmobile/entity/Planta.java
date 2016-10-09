package com.andmore.parkitmobile.entity;

import java.io.Serializable;

public class Planta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5566997782003844547L;
	
	private int id;
	private String nombre;
	private Centro_Comercial centro_Comercial;
	
	
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
	public Centro_Comercial getCentro_Comercial() {
		return centro_Comercial;
	}
	public void setCentro_Comercial(Centro_Comercial centro_Comercial) {
		this.centro_Comercial = centro_Comercial;
	}
	
	

}
