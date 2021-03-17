package com.CamelArquitectura.Modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "factura")
@XmlAccessorType(XmlAccessType.FIELD)

public class FacturaElectronica {
	private String pNombre;
	private String pId;
	private String pCorreoElectronico;
	private int pMonto;
	private String pComercio;
	
	public String getpNombre() {
		return pNombre;
	}
	public void setpNombre(String pNombre) {
		this.pNombre = pNombre;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getpCorreoElectronico() {
		return pCorreoElectronico;
	}
	public void setpCorreoElectronico(String pCorreoElectronico) {
		this.pCorreoElectronico = pCorreoElectronico;
	}
	public int getpMonto() {
		return pMonto;
	}
	public void setpMonto(int pMonto) {
		this.pMonto = pMonto;
	}
	public String getpComercio() {
		return pComercio;
	}
	public void setpComercio(String pComercio) {
		this.pComercio = pComercio;
	}
	
}