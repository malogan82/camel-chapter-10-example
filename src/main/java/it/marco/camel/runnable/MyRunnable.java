package it.marco.camel.runnable;

import org.apache.camel.main.Main;

public class MyRunnable implements Runnable {
	
	Main main;
	
	public MyRunnable(Main main) {
		this.main = main;
	}

	public void run() {
		try {
			main.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
