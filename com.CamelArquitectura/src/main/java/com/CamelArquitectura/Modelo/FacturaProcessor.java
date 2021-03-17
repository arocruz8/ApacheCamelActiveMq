package com.CamelArquitectura.Modelo;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FacturaProcessor implements Processor {

  public void process(Exchange exchange) throws Exception {
		FacturaElectronica facturaElectronica = exchange.getIn().getBody(FacturaElectronica.class); 
		exchange.getIn().setBody(facturaElectronica); 
	}
	

}