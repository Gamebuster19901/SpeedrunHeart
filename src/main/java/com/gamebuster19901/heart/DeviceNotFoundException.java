package com.gamebuster19901.heart;

import java.io.IOException;

@SuppressWarnings("serial")
public class DeviceNotFoundException extends IOException {

	private final String port;
	
	public DeviceNotFoundException(String name) {
		super("Could not connect to device on port: " + name);
		this.port = name;
	}
	
	public String getPort() {
		return port;
	}
	
}
