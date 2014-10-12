package com.example.recorder;

import com.example.important.Constants;
import com.example.important.MessagesLog;

public class FrequencyTime {
	private final static String TAG = "FREQ_TIME";
	private long start;
	private long end = 0;
	private int frequency;
	private int counter;
	private int nr_sign = 0;
	private String sign;
	private StringBuilder sb;
	
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
	
	public void increaseCount(){
		counter++;
		
	}
	
	
	public String foundAndReturnChar(){
		if(counter == 3){
			counter = 1;
			nr_sign++;
			return sign;
		}
		return Constants.NOEND_STR;
	}
	
	public String returnChar(){
		if(counter == 1 && nr_sign > 1){
			return Constants.NOEND_STR;
		}else if(counter >= 1 && counter <= 3){
			return sign;
		}
		return Constants.NOEND_STR;
	}
	
	
//	public char computeAndReturnChar() {
//		long diff = end - start;
//		long ten_percent = Constants.DEFAULT_GEN_DURATION / 10;
//		if (end == 0) {
//			return Constants.NOEND;
//		} else {
//			if (diff < Constants.DEFAULT_GEN_DURATION + ten_percent
//					&& diff > Constants.DEFAULT_GEN_DURATION + ten_percent) {
//				for (int i = 0; i < Constants.FREQUENCIES.length; i++) {
//					if (Constants.FREQUENCIES[i] + 50 > frequency
//							&& Constants.FREQUENCIES[i] - 50 < frequency) {
//						return Constants.STANDARD_ALPHABET[i];
//					}
//				}
//			}
//		}
//		return Constants.NOEND;
//	}
//	
//	public String computeAndReturnString(){
//		StringBuilder sb = new StringBuilder();
//		long diff = end - start;
//		float number_of_sign = 0;
//		int number_of_sign_rounded = 0;
//		for (int i = 0; i < Constants.FREQUENCIES.length; i++) {
//			if (Constants.FREQUENCIES[i] + 20 > frequency
//					&& Constants.FREQUENCIES[i] - 35 < frequency) {
//				if(diff > Constants.DEFAULT_GEN_DURATION){
//					number_of_sign = (float) diff / Constants.DEFAULT_GEN_DURATION;
//					number_of_sign_rounded = Math.round(number_of_sign);
//					for (int j = 0; j < number_of_sign_rounded; j++) {
//						sb.append(Constants.STANDARD_ALPHABET[i]);
//					}
//				}else{
//				
//					sb.append(Constants.STANDARD_ALPHABET[i]);
//				}
//				return sb.toString();
//			}
//		}
//		return Constants.NOEND_STR;
//	}

}
