package com.gamebuster19901.heart.gui;

import java.util.List;

import javax.swing.SwingWorker;

import com.gamebuster19901.heart.PulseDevice.PulseListener;

public final class MonitorWorker extends SwingWorker<Void, Integer> implements PulseListener {

	private final Monitor monitor;
	
	public MonitorWorker(Monitor monitor) {
		this.monitor = monitor;
	}
	
	public void onReceive(int[] data) {
		publish(data[1]);
	}
	
	@Override
	protected void process(List<Integer> chunks) {
		for(int i = 0; i < chunks.size(); i++) {
			monitor.getDataSet().advanceTime();
			monitor.getDataSet().appendData(new float[] {chunks.get(i)});
		}
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		monitor.pulseDevice.connect();
		return null;
	}
	
}
