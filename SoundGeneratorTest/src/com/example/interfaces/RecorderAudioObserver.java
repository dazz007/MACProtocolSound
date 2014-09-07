package com.example.interfaces;

import com.example.important.Buffer;
import com.example.recorder.RecorderAudio;


public interface RecorderAudioObserver {
//	public void sendDataToGraph(int[] data);
//	
//	public void sendDataToGraphByte(byte [] data);
	
	public void putBufferToQueue(Buffer buffer);
	
	public void setStopStatus();
	
	public void setSubject(RecorderAudio sub);
}
