package com.example.graphic;

import com.example.important.SoundGenSubject;

public interface SoundGenObserver {
	public void updateLineGraph(int[] data);
	
	public void setSubject(SoundGenSubject sub);
}
