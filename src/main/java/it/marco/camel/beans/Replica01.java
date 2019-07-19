package it.marco.camel.beans;

public class Replica01 {
	
	public String convertBody(String body) {
		return String.format("Replica01 says %s",body);
	}

}
