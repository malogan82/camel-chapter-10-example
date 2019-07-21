package it.marco.camel.beans;

import org.apache.camel.Exchange;

public class MyBean {
	
	public boolean isDuplicated(Exchange exchange) {
		return "true".equals(exchange.getProperty(Exchange.DUPLICATE_MESSAGE,String.class));
	}

}
