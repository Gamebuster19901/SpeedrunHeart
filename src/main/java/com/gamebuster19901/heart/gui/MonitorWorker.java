package com.gamebuster19901.heart.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.SwingWorker;

import com.gamebuster19901.heart.Main;
import com.gamebuster19901.heart.PulseDevice.PulseListener;

public final class MonitorWorker extends SwingWorker<Void, Integer> implements PulseListener {

	private final Monitor monitor;
	
	public MonitorWorker(Monitor monitor) {
		this.monitor = monitor;
	}
	
	public void onReceive(int[] data) {
		publish(data[1]);
		BufferedImage image = new BufferedImage(monitor.getWidth(), monitor.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setFont(g2.getFont().deriveFont(90f));
		Color color;
		g2.setColor(Color.green);
		String text;
		if(data[0] == 0) {
			text = "FLATLINED";
			color = Color.RED;
		}
		else {
			text = "" + data[0];
			if(data[0] <= 45) {
				color = Color.RED;
			}
			else if(data[0] <= 55) {
				color = Color.YELLOW;
			}
			else if(data[0] <= 85) {
				color = Color.GREEN;
			}
			else if (data [0] <= 105) {
				color = Color.YELLOW;
			}
			else if (data[0] <= 125) {
				color = Color.RED;
			}
			else {
				color = Main.GBColor.brighter().brighter();
			}
		}
		g2.setColor(color);
		FontMetrics fontMetrics = g2.getFontMetrics();
		int x = monitor.getWidth() / 2 - fontMetrics.stringWidth(text) / 2;
		int y = monitor.getHeight() - fontMetrics.getHeight();
		g2.drawString(text, x, y);
		monitor.getChart().getPlot().setBackgroundImage(image);
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
