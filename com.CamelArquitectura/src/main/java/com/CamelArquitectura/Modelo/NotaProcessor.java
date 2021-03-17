package com.CamelArquitectura.Modelo;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class NotaProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {
		Nota nota = exchange.getIn().getBody(Nota.class); //extrae los datos en formato xml
		exchange.getIn().setBody(nota); //convierte los datos a json
	}

}