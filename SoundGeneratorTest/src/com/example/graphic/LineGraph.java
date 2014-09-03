package com.example.graphic;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.SoundGenSubject;
import com.example.important.SoundGenerator;
import com.example.recorder.DecoderSubject;
import com.example.recorder.VoiceRecSubject;

import android.content.Context;
import android.graphics.Color;

public class LineGraph implements VoiceRecObserver {

	private GraphicalView view;
	private final static String TAG = "SoundGenerator";
	private TimeSeries dataset = new TimeSeries("Sound wave");
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private int state;
	private Thread thread_draw;
	private XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be
																// used to
																// customize
																// line 1
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds
																					// a
																					// collection
																					// of
																					// XYSeriesRenderer
																					// and
																					// customizes
																					// the
																					// graph

	// private SoundGenSubject sgs;
	private VoiceRecSubject voice_recognition_subject;
	private DecoderSubject decoder_subject;

	public LineGraph() {
		// Add single dataset to multiple dataset
		mDataset.addSeries(dataset);

		// Customization time for line 1!
		renderer.setColor(Color.WHITE);
		// renderer.setPointStyle(PointStyle.);
		// renderer.setFillPoints(true);

		// Enable Zoom
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setXTitle("Frequency");
		mRenderer.setXLabels(0);
		mRenderer.setYLabels(0);
		mRenderer.setYTitle("Amplitude");
		mRenderer.setShowAxes(false);

		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
		// Add single renderer to multiple renderer
		mRenderer.addSeriesRenderer(renderer);

		state = Constants.STOP_STATE;
	}

	public GraphicalView getView(Context context) {
		view = ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}

	public void addNewPoints(Point p) {
		dataset.add(p.getX(), p.getY());
	}

	private void updateLineGraph(int[] data) {
		MessagesLog.d(TAG, "Wesz³o w update LineGraph");
		int index = 0;
		// dataset.clear();
		int start_point = 200;
		for (int i = start_point; i < start_point + 150; i++) {
			dataset.add(index++, data[i] / 1000);
		}
		// view.repaint();
	}

	@Override
	public void setSubject(VoiceRecSubject vrs) {
		voice_recognition_subject = vrs;

	}

	private void updateLineGraphByte(short[] data) {
		// MessagesLog.d(TAG, "Wesz³o w update LineGraph");
		int index = 0;
		dataset.clear();
		int start_point = 200;

		// for(int i = start_point; i < start_point+150; i++){
		// int b_to_int = data[i];
		// dataset.add(index++, b_to_int/1000);
		// }
		for (int i = 200; i < 1200; i++) {
			// int b_to_int = data[i];
			dataset.add(index++, data[i]);
		}
		// for(int i = 0; i < data.length; i++){
		// // int b_to_int = data[i];
		// dataset.add(i, data[i]);
		// }
		view.repaint();

	}
	
	
	private void updateLineGraphFFT(double[] data) {
		// MessagesLog.d(TAG, "Wesz³o w update LineGraph");
		int index = 0;
		dataset.clear();
		int start_point = 200;

		// for(int i = start_point; i < start_point+150; i++){
		// int b_to_int = data[i];
		// dataset.add(index++, b_to_int/1000);
		// }
		for (int i = 0; i < 500; i++) {
			// int b_to_int = data[i];
			dataset.add(index++, data[i]);
		}
		// for(int i = 0; i < data.length; i++){
		// // int b_to_int = data[i];
		// dataset.add(i, data[i]);
		// }
		view.repaint();

	}

	public void start(boolean fft) {
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			if (!fft) {
				thread_draw = new Thread() {
					@Override
					public void run() {

						while (state == Constants.START_STATE) {
							Buffer buffer = voice_recognition_subject
									.getBufferForGraphQueue();
							if (buffer != null) {
								updateLineGraphByte(buffer.getBufferShort());
							}
						}
					}
				};
			} else {
				thread_draw = new Thread() {
					@Override
					public void run() {

						while (state == Constants.START_STATE) {
							Buffer buffer = decoder_subject
									.getBufferFFTForGraphQueue();
							if (buffer != null) {
								updateLineGraphFFT(buffer.getBufferFFT());
							}
						}
					}
				};
			}
			if (thread_draw != null) {
				thread_draw.start();
			}

		}
	}
	
	public void setDecSubject(DecoderSubject decoder){
		this.decoder_subject = decoder;
	}
}
