package com.CamelArquitectura.Route;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.CamelArquitectura.Modelo.Aggregation;
import com.CamelArquitectura.Modelo.FacturaElectronica;
import com.CamelArquitectura.Modelo.FacturaProcessor;
import com.CamelArquitectura.Modelo.Nota;
import com.CamelArquitectura.Modelo.NotaProcessor;

public class CamelRoute extends RouteBuilder {
	private boolean bandera = true;
	@Override
	public void configure() throws Exception {
		//Busqueda principal de los archivos base
		from("file:C:/inputFolder").to("jms:queue:DatosIniciales");
		
		//Enruta los archivos
		MessageRouter();

		/*El proceso de una factura electronica, nota de credito o debito y tiquete electronico*/
		ProcesoFactura();
		ProcesoNota();
		ProcesoTiquete();
	}

	private void MessageRouter() {
		/*Identifica el tipo de archivo y los enruta a la cola que pertencen*/
		from("jms:queue:DatosIniciales").choice().when(header("CamelFileName")
			.isEqualTo("FacturaElectronica.txt")).to("jms:queue:FacturasElectronicas")
			.when((header("CamelFileName").isEqualTo("Tiquete.txt"))).to("jms:queue:Tiquetes")
			.when((header("CamelFileName").isEqualTo("NotaCD.txt"))).to("jms:queue:Notas")
			.end();
	}

	private void ProcesoNota() throws JAXBException {
		/*los datos de los txt se encuentran en xml*/
		JaxbDataFormat xmlDataFormat = new JaxbDataFormat(); 
		JAXBContext con = JAXBContext.newInstance(Nota.class);
		xmlDataFormat.setContext(con);
		/*libreria jackson sirve para parsear los datos de xml a json*/
		JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Nota.class);

		//Extrae los archivos de la cola de notas de credito o debito
		//convierte los datos de xml a json
		from("jms:queue:Notas").doTry().unmarshal(xmlDataFormat).process(new NotaProcessor()).marshal(jsonDataFormat) 
		//envia los datos a la cola de Notas Json
			.to("jms:queue:Notas-JSON").doCatch(Exception.class).process(new Processor() { 
				public void process(Exchange exchange) throws Exception {
					Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
					System.out.println(cause);
				}
			});
		/*Endpoint especifico en la carpeta de aprobacion de facturas*/
		from("jms:queue:Notas-JSON").to("file:C:/CamelArquitectura/AprobacionFacturas").end(); 
	}

	private void ProcesoFactura() throws JAXBException {
		/*Traduce los datos de XML a JSON
		Formato XML de datos*/
		JaxbDataFormat xmlDataFormat = new JaxbDataFormat();
		JAXBContext con = JAXBContext.newInstance(FacturaElectronica.class);
		xmlDataFormat.setContext(con);

		/*Formato JSON de datos*/
		JacksonDataFormat jsonDataFormat = new JacksonDataFormat(FacturaElectronica.class);
		/*convierte los datos de xml a json*/
		from("jms:queue:FacturasElectronicas").doTry().unmarshal(xmlDataFormat).process(new FacturaProcessor()).marshal(jsonDataFormat)
			/*envia los datos a la cola de Facturas Electronicas Json*/
			.to("jms:queue:FE-JSON").doCatch(Exception.class).process(new Processor() { 
				public void process(Exchange exchange) throws Exception {
					Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
					System.out.println(cause);
				}
			});
		
		/*Agrega datos al archivo del proveedor*/
		AggregationStrategy agregationStrategy = new Aggregation();
		from("jms:queue:FE-JSON")
		.pollEnrich("file:C:/CamelArquitectura", agregationStrategy)
		/*Se envia la factura almacenada a la cola en activemq*/
		.to("jms:queue:FE-Enrich","file:C:/CamelArquitectura/Sistema","jms:queue:FE-Total"); 
		
		//Se extrae monto y firma para validar
		from("jms:queue:FE-Enrich").split().tokenize("\n").process(new Processor() { 
	        public void process(Exchange exchange) throws Exception {
	            String data = exchange.getIn().getBody(String.class);
	            if(data.contains("pNomProveedor")) {
	            	if(data.contains("pFirma") && data.contains("pMontoCobro")) { 
	            		bandera = true;
	            	}else {
	            		bandera = false;
	            	}
	            }
	       }
	    });
		
		//Se envia a los endpoints especificos
		if(bandera == true) { 
			System.out.println("Notificación que fue rechazada al proveedor!!!");
			from("jms:queue:FE-Total").to("file:C:/CamelArquitectura/MinisterioHacienda").end();
		}else { 
			System.out.println("Notificación que fue rechazada al proveedor!!!");
		}
	}
	
	private void ProcesoTiquete() {
		from("jms:queue:Tiquetes").to("file:C:/CamelArquitectura/Boletos").end(); 
	}
	
}