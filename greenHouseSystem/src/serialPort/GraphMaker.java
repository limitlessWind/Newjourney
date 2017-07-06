package serialPort;

/* --------------------
* MemoryUsageDemo.java
* --------------------
* (C) Copyright 2002-2006, by Object Refinery Limited.
*/

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * A demo application showing a dynamically updated chart that displays the
 * current JVM memory usage.
 * <p>
 * IMPORTANT NOTE: THIS DEMO IS DOCUMENTED IN THE JFREECHART DEVELOPER GUIDE. DO
 * NOT MAKE CHANGES WITHOUT UPDATING THE GUIDE ALSO!!
 */
public class GraphMaker extends JPanel {
	/** Time series for total memory used. */
	private TimeSeries total;
	private NumberAxis range;

	/**
	 * Creates a new application.
	 *
	 * @param maxAge
	 *            the maximum age (in milliseconds).
	 */
	public GraphMaker(String name) {
		super(new BorderLayout());
		// create two series that automatically discard data more than 30
		// seconds old...
		this.total = new TimeSeries(name, Millisecond.class);
		this.total.setMaximumItemAge(60000);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(this.total);

		DateAxis domain = new DateAxis();
		range = new NumberAxis(name);
		domain.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
		range.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
		domain.setLabelFont(new Font("宋体", Font.BOLD, 14));
		range.setLabelFont(new Font("宋体", Font.BOLD, 14));
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);

		renderer.setSeriesPaint(0, Color.blue);
		renderer.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		XYPlot plot = new XYPlot(dataset, domain, range, renderer);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));		//gap between the data area and the axes图片区与坐标轴之间的距离
		domain.setAutoRange(true);
		domain.setLowerMargin(0.0);
		domain.setUpperMargin(0.0);
		domain.setTickLabelsVisible(true);
//		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		JFreeChart chart = new JFreeChart(null, null, plot, true);
		chart.setBackgroundPaint(Color.white);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4),
				BorderFactory.createLineBorder(Color.black)));
		add(chartPanel);
	}

	/**
	 * Adds an observation to the ’total memory’ time series.
	 *
	 * @param y
	 *            the total memory used.
	 */
	public void addTotalObservation(double y) {
		this.range.setRangeAboutValue(y, y / 4);		//maybe low efficient, should be remove
		this.total.add(new Millisecond(), y);
		
	}



	/**
	 * The data generator.
	 */
	
	/*
	class DataGenerator extends Timer implements ActionListener {
		*//**
		 * Constructor.
		 *
		 * @param interval
		 *            the interval (in milliseconds)
		 *//*
		DataGenerator(int interval) {
			super(interval, null);
			addActionListener(this);
		}

		*//**
		 * Adds a new free/total memory reading to the dataset.
		 *
		 * @param event
		 *            the action event. CHAPTER 10. DYNAMIC CHARTS 77
		 *//*
		public void actionPerformed(ActionEvent event) {
			long f = Runtime.getRuntime().freeMemory();
			long t = Runtime.getRuntime().totalMemory();
			addTotalObservation(t);
			addFreeObservation(f);
		}
	}

	*//**
	 * Entry point for the sample application.
	 *
	 * @param args
	 *            ignored.
	 *//*
	public static void main(String[] args) {
		JFrame frame = new JFrame("Memory Usage Demo");
		MemoryUsageDemo panel = new MemoryUsageDemo(60000);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setBounds(200, 120, 600, 280);
		frame.setVisible(true);
		panel.new DataGenerator(1000).start();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}*/
}
