package com.gamebuster19901.heart.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.Range;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;

import com.gamebuster19901.heart.Main;
import com.gamebuster19901.heart.PulseDevice;
import com.gamebuster19901.heart.PulseDevice.PulseListener;

@SuppressWarnings("serial")
public class Monitor extends ApplicationFrame implements PulseListener {

	PulseDevice pulseDevice;
	private DynamicTimeSeriesCollection dataset;
	private JFreeChart chart;
	private MonitorWorker worker;
	
	public Monitor(PulseDevice pulseDevice) throws IOException, InterruptedException {
		super("test");
		this.pulseDevice = pulseDevice;
		dataset = new DynamicTimeSeriesCollection(1, 2000, new Second());
		dataset.setTimeBase(new Second());
		dataset.addSeries(new float[] {}, 0, "Refractive Index");
		chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, false, false);
		chart.setBorderVisible(false);
		
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.BLACK);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);
		plot.getDomainAxis().setAutoRange(true);
		plot.getDomainAxis().setVisible(false);
		plot.getRangeAxis().setRange(new Range(0, 1024), false, true);
		plot.getRangeAxis().setVisible(false);
		plot.setOutlinePaint(null);
		plot.setInsets(new RectangleInsets() {
			public void trim(Rectangle2D area) {}
		});
		
		this.setSize(1000, 250);
		this.setResizable(false);
		
		plot.getRenderer().setSeriesStroke(0, new BasicStroke(3));
		plot.getRenderer().setSeriesPaint(0, Main.GBColor);
		
		ChartPanel panel = new ChartPanel(chart);
		this.add(panel);
		setVisible(true);
	}
	
	@Override
	public void update(Graphics g) {
		super.update(g);
		chart.setNotify(false);
	}

	@Override
	public void onReceive(int[] data) {
		worker.onReceive(data);
	}
	
	public JFreeChart getChart() {
		return chart;
	}
	
	public DynamicTimeSeriesCollection getDataSet() {
		return dataset;
	}
}
