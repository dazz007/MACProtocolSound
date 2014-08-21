package com.example.graphic;

import com.example.recorder.VoiceRecSubject;


public interface VoiceRecObserver {
	public void updateLineGraph(int[] data);
	
	public void updateLineGraphByte(byte [] data);
	
	public void setSubject(VoiceRecSubject sub);
}
