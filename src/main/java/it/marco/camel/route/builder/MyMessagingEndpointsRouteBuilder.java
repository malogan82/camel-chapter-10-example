package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

import it.marco.camel.processors.MyProcessor;

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
		
		from(String.format("activemq:dispatcher?selector=%s",java.net.URLEncoder.encode("CountryCode='US'","UTF-8")))
			.bean("replica01");
		
		from(String.format("activemq:dispatcher?selector=%s",java.net.URLEncoder.encode("CountryCode='IE'","UTF-8")))
			.bean("replica02");
		
		from(String.format("activemq:dispatcher?selector=%s",java.net.URLEncoder.encode("CountryCode='DE'","UTF-8")))
			.bean("replica03");
		
		from("seda:a")
			.filter(header("CountryCode").isEqualTo("US"))
			.process(new MyProcessor());
		
		from("activemq:topic:news?clientId=conn01&durableSubscriptionName=John.Doe")
	    	.bean("replica01")
	    	.log("from replica01 ----------> ${body}");
			
	}

}
