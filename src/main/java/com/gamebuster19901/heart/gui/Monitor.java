package com.gamebuster19901.heart.gui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOError;
import java.io.IOException;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.Range;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Millisecond;

import com.gamebuster19901.heart.PulseDevice;
import com.gamebuster19901.heart.PulseDevice.PulseListener;

@SuppressWarnings("serial")
public class Monitor extends ApplicationFrame implements PulseListener {

	PulseDevice pulseDevice;
	private DynamicTimeSeriesCollection dataset;
	private JFreeChart chart;
	
	public Monitor(PulseDevice pulseDevice) throws IOException, InterruptedException {
		super("test");
		this.pulseDevice = pulseDevice;
		dataset = new DynamicTimeSeriesCollection(1, 2000, new Millisecond());
		dataset.setTimeBase(new Millisecond());
		dataset.addSeries(new float[] {}, 0, "Refractive Index");
		chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, false, false);
		chart.setNotify(false);
		final XYPlot plot = chart.getXYPlot();
		plot.getDomainAxis().setAutoRange(true);
		plot.getRangeAxis().setRange(new Range(0, 1024), false, true);
		
		this.add(new ChartPanel(chart));
		setVisible(true);
		pulseDevice.addListener(this);
		Timer timer = new Timer(5, (e) -> {
			chart.setNotify(true);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			chart.setNotify(false);
		});
		timer.start();
	}
	
	@Override
	public void update(Graphics g) {
		super.update(g);
		chart.setNotify(false);
	}

	@Override
	public void onReceive(int data) {
		EventQueue.invokeLater( () -> {
		dataset.advanceTime();
		dataset.appendData(new float[] {data});
		});
	}

	
	
}
