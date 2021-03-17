package com.CamelArquitectura.Modelo;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class Aggregation implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (newExchange == null) {
			return oldExchange;
		}
		String factura = oldExchange.getIn().getBody(String.class);
		String proveedor = newExchange.getIn().getBody(String.class);
		String body = factura + "\n" + proveedor;
		oldExchange.getIn().setBody(body);
		System.out.println("Notificacion factura fue procesada!!!");
		return oldExchange;
	}

}

