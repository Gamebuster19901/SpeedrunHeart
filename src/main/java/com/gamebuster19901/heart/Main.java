package com.gamebuster19901.heart;

import java.io.IOException;

import jssc.SerialPortException;

public class Main {

	public static void main(String[] args) throws InterruptedException, SerialPortException, IOException {
		HC06 monitor = HC06.tryGetHC06("/dev/ttyACM0", 10, true);
		
		while(true) {
			System.out.println(monitor.read() + " r");
		}
	}
	
}
