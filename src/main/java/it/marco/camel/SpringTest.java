package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import it.marco.camel.runnable.MyRunnable;

public class SpringTest {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTest.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context.xml");
		try {
			CamelContext camelContext = SpringCamelContext.springCamelContext(appContext);
			Main main = new Main();
			main.getCamelContexts().add(camelContext);
			MyRunnable runnable = new MyRunnable(main);
			Thread thread = new Thread(runnable);
			thread.run();
			while(!main.isStarted()) {
				if(main.isStarted()) {
					break;
				}
			}
			LOGGER.info("MAIN STARTED");
			ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
//			String responseConcurrent1 = producerTemplate.requestBody("activemq:HighVolumeQ-concurrent","TESTCONCURRENT1",String.class);
//			String responseConcurrent2 = producerTemplate.requestBody("activemq:HighVolumeQ-concurrent","TESTCONCURRENT2",String.class);
//			String responseConcurrent3 = producerTemplate.requestBody("activemq:HighVolumeQ-concurrent","TESTCONCURRENT3",String.class);
//			producerTemplate.sendBody("activemq:topic:foo","TEST-TOPIC-FOO-1");
//			producerTemplate.sendBody("activemq:topic:foo","TEST-TOPIC-FOO-2");
//			LOGGER.info(responseConcurrent1);
//			LOGGER.info(responseConcurrent2);
//			LOGGER.info(responseConcurrent3);
			producerTemplate.sendBody("direct:start-virtual-topic","TEST-VIRTUAL-TOPIC-1");
			producerTemplate.sendBody("direct:start-virtual-topic","TEST-VIRTUAL-TOPIC-2");
			producerTemplate.sendBodyAndHeader("direct:start-idempotent","TEST-IDEMPOTENT-1","messageId","MESSAGEID");
			producerTemplate.sendBodyAndHeader("direct:start-idempotent","TEST-IDEMPOTENT-2","messageId","MESSAGEID");
			try {
				Thread.sleep(50000);
				main.stop();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(),e);
			}
			LOGGER.info("MAIN STOPPED");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}
