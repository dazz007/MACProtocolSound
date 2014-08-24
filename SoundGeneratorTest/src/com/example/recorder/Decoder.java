package com.example.recorder;

import com.example.graphic.VoiceRecObserver;
import com.example.important.Buffer;
import com.example.important.Constants;

public class Decoder implements VoiceRecObserver{
	private VoiceRecSubject vrs;
	private int state;

	@Override
	public void setSubject(VoiceRecSubject sub) {
		vrs = sub;
		
	}
	
	public void start(){
		if(state == Constants.STOP_STATE){
			state = Constants.START_STATE;
					while(state == Constants.START_STATE){
						Buffer buffer = vrs.getBufferForDecoderQueue();
						if(buffer != null){
							analyse(buffer);
						}
					}
		}
	}
	
	
	private void analyse(Buffer buffer){
		
	}
}
