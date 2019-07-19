package it.marco.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyProcessor implements Processor {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		LOGGER.info(String.format("MyProcessor says %s", exchange.getIn().getBody(String.class)));
	}

}
