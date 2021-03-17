package com.CamelArquitectura.Main;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import com.CamelArquitectura.Route.CamelRoute;

public class MainAppCamel {
	public static void main(String[] args) {
		//Crea el objeto SimpleRouteBuilder para inciar el proceso
		CamelRoute routeBuilder = new CamelRoute();
		//Crea el contexto
		CamelContext ctx = new DefaultCamelContext();

		//Configura el componente jms
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
		ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		try {
			//agrega la rutas
			ctx.addRoutes((RoutesBuilder) routeBuilder);
			ctx.start();
			Thread.sleep(5 * 60 * 1000);
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
