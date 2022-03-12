package com.gamebuster19901.heart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Timer;

import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;

import com.gamebuster19901.heart.gui.Monitor;
import com.gamebuster19901.heart.gui.MonitorWorker;

import jssc.SerialPortException;

public class Main {

	public static final Color GBColor = Color.decode("0x7F3FA5");
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public static void main(String[] args) throws InterruptedException, SerialPortException, IOException {
		PulseDevice pulseDevice = PulseDevice.tryGetHC06("/dev/ttyACM1", 10, true);
		
		Monitor monitor = new Monitor(pulseDevice, 2000);
		MonitorWorker worker = new MonitorWorker(monitor);
		pulseDevice.addListener(worker);
		worker.execute();
		Timer timer = new Timer(1000, (e) -> {
			LocalDateTime now = LocalDateTime.now();
			ValueMarker marker = new ValueMarker(monitor.getChart().getXYPlot().getDomainAxis(0).getUpperBound());
			marker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
			marker.setStroke(new BasicStroke(2));
			marker.setLabelAnchor(RectangleAnchor.BOTTOM);
			marker.setLabelBackgroundColor(Color.BLACK);
			marker.setLabelPaint(GBColor);
			marker.setLabelFont(marker.getLabelFont().deriveFont(17f));
			marker.setLabel(formatter.format(now));
			monitor.getChart().getXYPlot().addDomainMarker(0, marker, Layer.BACKGROUND);
		});
		timer.start();
		
		
		//while(true) {
		//	System.out.println(pulseDevice.read() + " r");
		//}
	}
	
}
