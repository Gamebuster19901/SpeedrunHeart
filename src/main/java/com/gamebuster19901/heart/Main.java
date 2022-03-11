package com.gamebuster19901.heart;

import java.io.IOException;

import com.gamebuster19901.heart.gui.Monitor;
import com.gamebuster19901.heart.gui.MonitorWorker;

import jssc.SerialPortException;

public class Main {

	public static void main(String[] args) throws InterruptedException, SerialPortException, IOException {
		PulseDevice pulseDevice = PulseDevice.tryGetHC06("/dev/ttyACM1", 10, true);
		
		Monitor monitor = new Monitor(pulseDevice);
		MonitorWorker worker = new MonitorWorker(monitor);
		pulseDevice.addListener(worker);
		worker.execute();
		
		
		//while(true) {
		//	System.out.println(pulseDevice.read() + " r");
		//}
	}
	
}
