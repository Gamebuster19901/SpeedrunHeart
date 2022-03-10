package com.gamebuster19901.heart;

import java.awt.EventQueue;
import java.io.IOException;

import com.gamebuster19901.heart.gui.Monitor;

import jssc.SerialPortException;

public class Main {

	public static void main(String[] args) throws InterruptedException, SerialPortException, IOException {
		PulseDevice pulseDevice = PulseDevice.tryGetHC06("/dev/ttyACM1", 10, true);
		
		EventQueue.invokeLater(() -> {
			Monitor monitor;
			try {
				monitor = new Monitor(pulseDevice);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		
		
		//while(true) {
		//	System.out.println(pulseDevice.read() + " r");
		//}
	}
	
}
