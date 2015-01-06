package com.example.recorder;

import com.example.important.Constants;
import com.example.important.MessagesLog;

public class FrequencyTime {
	private final static String TAG = "FREQ_TIME";
	private long start;
	private long end = 0;
	private int frequency;
	private int counter; // counter which measures appearance of frequency in frames
	private int nr_sign = 0;
	private String sign;
	private StringBuilder sb;
	
	/**
	 * Constructor FrequencyTime. Class responsible for hold recorded frequency and calculate apperance of this frequency,
	 */
	public FrequencyTime(){
		counter = 0;
		sign =  "";
		sb = new StringBuilder();
	}
	
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public int getFrequency() {
		return frequency;
	}

	/**
	 * Set frequency
	 * @param frequency - founded frequency
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
		counter++;
		nr_sign++;
		for (int i = 0; i < Constants.FREQUENCIES.length; i++) {
			if (Constants.FREQUENCIES[i] + Constants.BORDER > frequency
					&& Constants.FREQUENCIES[i] - Constants.BORDER < frequency) {
				sign = String.valueOf(Constants.STANDARD_ALPHABET[i]);
				break;
			}
			
		}
	}
	
	/**
	 * Increase counter
	 */
	public void increaseCount(){
		counter++;
		
	}
	
	/**
	 * Check counters and return char
	 * @return sign - sign adequate to found frequency
	 */
	public String foundAndReturnChar(){
		if(counter == 3){
			counter = 1;
			nr_sign++;
			return sign;
		}
		return Constants.NOEND_STR;
	}
	
	/**
	 * Found and return char
	 * @return sign - sign adequate to found frequency
	 */
	public String returnChar(){
		if(counter == 1 && nr_sign > 1){
			return Constants.NOEND_STR;
		}else if(counter >= 1 && counter <= 3){
			return sign;
		}
		return Constants.NOEND_STR;
	}
	

}
