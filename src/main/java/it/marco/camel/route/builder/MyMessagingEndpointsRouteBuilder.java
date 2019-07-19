package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

public class MyMessagingEndpointsRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("activemq:HighVolumeQ")
			.bean("replica01");
		
		from("activemq:HighVolumeQ")
			.bean("replica02");
		
		from("activemq:HighVolumeQ")
			.bean("replica03");
		
		from("activemq:HighVolumeQ-concurrent?concurrentConsumers=3")
			.bean("replica01");
			
	}

}
