package com.gamebuster19901.heart;

import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class PulseDevice implements SerialPortEventListener {

	protected SerialPort serialPort;
	private StringBuilder data = new StringBuilder();
	private ArrayList<PulseListener> listeners = new ArrayList<PulseListener>();
	
	protected PulseDevice(SerialPort serialPort) throws SerialPortException {
		this.serialPort = serialPort;
		connect();
	}
	
	protected void connect() throws SerialPortException {
		if(serialPort.isOpened()) {
			throw new IllegalStateException(serialPort.getPortName() + " is already open!");
		}
		serialPort.openPort();
		serialPort.setParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
				parse(serialPort.readString().toCharArray());
			}
		} catch (SerialPortException e1) {
			e1.printStackTrace();
		}
	}
	
	public static PulseDevice getHC06(String portName) throws SerialPortException {
		String[] ports = SerialPortList.getPortNames();
		for(String s : ports) {
			if(s.equals(portName)) {
				System.out.println("Found device connected to port " + portName);
				return new PulseDevice(new SerialPort(s));
			}
		}
		System.out.println("Could not find device connected to port " + portName);
		return NullDevice.INSTANCE;
	}
	
	
	public static PulseDevice tryGetHC06(String portName, int seconds, boolean throwException) throws DeviceNotFoundException, InterruptedException, SerialPortException {
		PulseDevice hc06 = NullDevice.INSTANCE;
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
	
	public void parse(char[] chars) {
		for(char c : chars) {
			switch (c) {
				case '\n':
					send();
					data = new StringBuilder();
					break;
				case '\r':
					break; //ignore
				default:
					data.append(c);
			}
		}
	}

	public void send() {
		for(PulseListener listener : listeners) {
			listener.onReceive(Integer.parseInt(data.toString()));
		}
	}
	
	public boolean addListener(PulseListener listener) {
		return listeners.add(listener);
	}
	
	public static interface PulseListener {
		public void onReceive(int data);
	}
}
