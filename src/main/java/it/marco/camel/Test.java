package it.marco.camel;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import it.marco.camel.beans.Replica01;
import it.marco.camel.beans.Replica02;
import it.marco.camel.beans.Replica03;
import it.marco.camel.route.builder.MyMessagingEndpointsRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class Test {
	
	public static Logger LOGGER = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {
		Main main = new Main();
		ActiveMQJMSConnectionFactory activeMQJMSConnectionFactory = 
				new ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin");
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(activeMQJMSConnectionFactory);
		activeMQComponent.setMessageConverter(new SimpleMessageConverter());
		main.bind("activemq", activeMQComponent);
		main.bind("replica01", new Replica01());
		main.bind("replica02", new Replica02());
		main.bind("replica03", new Replica03());
		main.addRouteBuilder(new MyMessagingEndpointsRouteBuilder());
		MyRunnable runnable = new MyRunnable(main);
		Thread thread = new Thread(runnable);
		thread.run();
		while(!main.isStarted()) {
			if(main.isStarted()) {
				break;
			}
		}
		LOGGER.info("MAIN STARTED");
		CamelContext camelContext = main.getCamelContexts().get(0);
		ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
		String response1 = producerTemplate.requestBody("activemq:HighVolumeQ","TEST1",String.class);
		String response2 = producerTemplate.requestBody("activemq:HighVolumeQ","TEST2",String.class);
		String response3 = producerTemplate.requestBody("activemq:HighVolumeQ","TEST3",String.class);
		String responseConcurrent1 = producerTemplate.requestBody("activemq:HighVolumeQ-concurrent","TESTCONCURRENT1",String.class);
		String responseConcurrent2 = producerTemplate.requestBody("activemq:HighVolumeQ-concurrent","TESTCONCURRENT2",String.class);
		String responseConcurrent3 = producerTemplate.requestBody("activemq:HighVolumeQ-concurrent","TESTCONCURRENT3",String.class);
		LOGGER.info(response1);
		LOGGER.info(response2);
		LOGGER.info(response3);
		LOGGER.info(responseConcurrent1);
		LOGGER.info(responseConcurrent2);
		LOGGER.info(responseConcurrent3);
		producerTemplate.sendBodyAndHeader("activemq:dispatcher","TESTDISPATCHER1","CountryCode","US");
		producerTemplate.sendBodyAndHeader("activemq:dispatcher","TESTDISPATCHER2","CountryCode","IE");
		producerTemplate.sendBodyAndHeader("activemq:dispatcher","TESTDISPATCHER3","CountryCode","DE");
		producerTemplate.sendBodyAndHeader("seda:a","TESTDISPATCHER1","CountryCode","US");
		producerTemplate.sendBodyAndHeader("seda:a","TESTDISPATCHER2","CountryCode","IE");
		producerTemplate.sendBodyAndHeader("seda:a","TESTDISPATCHER3","CountryCode","DE");
		producerTemplate.sendBody("activemq:topic:news","TEST-TOPIC-1");
		producerTemplate.sendBody("activemq:topic:news-concurrent","TEST-TOPIC-CONCURRENT-1");
		producerTemplate.sendBody("activemq:topic:news-concurrent","TEST-TOPIC-CONCURRENT-2");
		producerTemplate.sendBody("activemq:topic:news-concurrent","TEST-TOPIC-CONCURRENT-3");
		try {
			Thread.sleep(20000);;
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
