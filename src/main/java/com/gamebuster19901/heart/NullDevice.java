package com.gamebuster19901.heart;

import jssc.SerialPortEvent;
import jssc.SerialPortException;

public class NullDevice extends PulseDevice {

	public static final NullDevice INSTANCE;
	static {
		try {
			INSTANCE = new NullDevice();
		} catch (SerialPortException e) {
			throw new AssertionError(e);
		}
	}
	
	@Override
	protected void connect() {
		//no-op
	}
	
	@Override
	public void disconnect() {
		//no-op
		new UnsupportedOperationException().printStackTrace();
	}
	
	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		throw new UnsupportedOperationException();
	}
	
	protected NullDevice() throws SerialPortException {
		super(null);
	}
	
	@Override
	public void finalize() {
		//no-op
	}

}
