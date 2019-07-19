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
		
		from("file://target/data?noop=true")
			.log("${header.CamelFileName}")
			.to("seda:fanout");
		
		from("seda:fanout?multipleConsumers=true")
			.bean("replica01")
			.log("from replica01 ----------> ${body}");
		
		from("seda:fanout?multipleConsumers=true")
			.bean("replica02")
			.log("from replica02 ----------> ${body}");
		
		from("seda:fanout?multipleConsumers=true")
			.bean("replica03")
			.log("from replica03 ----------> ${body}");
			
	}

}
