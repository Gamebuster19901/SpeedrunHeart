package com.gamebuster19901.heart;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class HC06 extends InputStream implements SerialPortEventListener {

	protected SerialPort serialPort;
	protected ConcurrentLinkedDeque<Character> queue = new ConcurrentLinkedDeque<Character>();
	
	protected HC06(SerialPort serialPort) throws SerialPortException {
		this.serialPort = serialPort;
		connect();
	}
	
	protected void connect() throws SerialPortException {
		if(serialPort.isOpened()) {
			throw new IllegalStateException(serialPort.getPortName() + " is already open!");
		}
		serialPort.openPort();
		serialPort.setParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
		serialPort.addEventListener(this);
	}
	
	public void disconnect() throws SerialPortException {
		if(serialPort.isOpened()) {
			serialPort.closePort();
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent e) {
		try {
			if(e.getEventType() == SerialPortEvent.RXCHAR) {
				char[] chars = serialPort.readString().toCharArray();
				for(char c : chars) {
					queue.add(c);
				}
			}
		} catch (SerialPortException e1) {
			e1.printStackTrace();
		}
	}
	
	public static HC06 getHC06(String portName) throws SerialPortException {
		String[] ports = SerialPortList.getPortNames();
		for(String s : ports) {
			if(s.equals(portName)) {
				System.out.println("Found device connected to port " + portName);
				return new HC06(new SerialPort(s));
			}
		}
		System.out.println("Could not find device connected to port " + portName);
		return NullDevice.INSTANCE;
	}
	
	
	public static HC06 tryGetHC06(String portName, int seconds, boolean throwException) throws DeviceNotFoundException, InterruptedException, SerialPortException {
		HC06 hc06 = NullDevice.INSTANCE;
		long s = 0;
		do {
			hc06 = getHC06(portName);
			if(s > seconds) {
				if(throwException) {
					throw new DeviceNotFoundException(portName);
				}
				break;
			}
			Thread.sleep(1000);
			s++;
		}
		while(hc06 == NullDevice.INSTANCE);
		return hc06;
	}
	
	@Override
	public void finalize() {
		try {
			disconnect();
		}
		catch(SerialPortException e) {
			//swallow
		}
	}

	@Override
	public int read() throws IOException {
		StringBuilder ret = new StringBuilder(4);
		
		while(ret.toString().trim().length() == 0) {
			for(Character c = getChar(); c != '\n' && c != '\r'; c = getChar()) {
				if(c == '\n' || c == '\r') {
					break;
				}
				ret.append(c);
			}
		}
		
		//System.out.println("queue size: " + queue.size());
		
		try {
			return Integer.parseInt(ret.toString());
		}
		catch(NumberFormatException e) {
			return -1;
		}
	}
	
	private Character getChar() {
		Character c = queue.pollFirst();
		while(c == null) {
			c = queue.pollFirst();
		}
		return c;
	}
	
}
