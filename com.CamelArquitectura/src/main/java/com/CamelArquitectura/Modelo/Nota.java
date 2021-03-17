package com.CamelArquitectura.Modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "nota")
@XmlAccessorType(XmlAccessType.FIELD)

public class Nota {
	private String pNombre;
	private String pDetalle;
	private double pMonto;
	private int pId;
	
	public String getpNombre() {
		return pNombre;
	}
	public void setpNombre(String pNombre) {
		this.pNombre = pNombre;
	}
	public String getpDetalle() {
		return pDetalle;
	}
	public void setpDetalle(String pDetalle) {
		this.pDetalle = pDetalle;
	}
	public double getpMonto() {
		return pMonto;
	}
	public void setpMonto(double pMonto) {
		this.pMonto = pMonto;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}

	
}
