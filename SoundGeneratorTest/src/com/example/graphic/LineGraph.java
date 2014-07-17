package com.example.graphic;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.important.MessagesLog;
import com.example.important.MyObserver;
import com.example.important.SoundGenSubject;
import com.example.important.SoundGenerator;

import android.content.Context;
import android.graphics.Color;

public class LineGraph implements SoundGenObserver{

	private GraphicalView view;
	private final static String TAG = "SoundGenerator";
	private TimeSeries dataset = new TimeSeries("Sound wave"); 
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	
	private XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used to customize line 1
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
	
	private SoundGenSubject sgs;
	
	public LineGraph()
	{
		// Add single dataset to multiple dataset
		mDataset.addSeries(dataset);
		
		// Customization time for line 1!
		renderer.setColor(Color.WHITE);
//		renderer.setPointStyle(PointStyle.);
//		renderer.setFillPoints(true);
		
		// Enable Zoom
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Frequency");
		mRenderer.setYTitle("Amplitude");
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
		// Add single renderer to multiple renderer
		mRenderer.addSeriesRenderer(renderer);
	}
	
	public GraphicalView getView(Context context) 
	{
		view =  ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}
	
	public void addNewPoints(Point p)
	{
		dataset.add(p.getX(), p.getY());
	}


	@Override
	public void updateLineGraph(int[] data) {
		MessagesLog.d(TAG, "Wesz³o w update LineGraph");
		int index = 0;
		dataset.clear();
		int start_point = 200;
		for(int i = start_point; i < start_point+150; i++){
			dataset.add(index++, data[i]/1000);
		}
		view.repaint();
	}

	@Override
	public void setSubject(SoundGenSubject sub) {
		sgs = sub;
		
	}
	
}
