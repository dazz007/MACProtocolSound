package com.example.recorder;

import com.example.important.Constants;
import com.example.important.MessagesLog;

public class FrequencyTime {
	private final static String TAG = "FREQ_TIME";
	private long start;
	private long end = 0;
	private int frequency;

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
	}

	public char computeAndReturnChar() {
		long diff = end - start;
		long ten_percent = Constants.DEFAULT_GEN_DURATION / 10;
		if (end == 0) {
			return Constants.NOEND;
		} else {
			if (diff < Constants.DEFAULT_GEN_DURATION + ten_percent
					&& diff > Constants.DEFAULT_GEN_DURATION + ten_percent) {
				for (int i = 0; i < Constants.FREQUENCIES.length; i++) {
					if (Constants.FREQUENCIES[i] + 20 > frequency
							&& Constants.FREQUENCIES[i] - 35 < frequency) {
						return Constants.AVAILABLE_SIGNS.charAt(i);
					}
				}
			}
		}
		return Constants.NOEND;
	}

	public String computeAndReturnString() {
		StringBuilder sb = new StringBuilder();
		long diff = end - start;
		long five_percent = Constants.DEFAULT_GEN_DURATION * 5 / 100;
		float number_of_sign = 0;
		int number_of_sign_rounded = 0;
		if (end == 0) {
			return Constants.NOEND_STR;
		} else {
			number_of_sign = (float) diff / Constants.DEFAULT_GEN_DURATION;
			//number_of_sign = diff / Constants.DEFAULT_GEN_DURATION;
			number_of_sign_rounded = Math.round(number_of_sign);
			MessagesLog.d(TAG, "diff " + Integer.toString((int)diff));
			MessagesLog.d(TAG, "number_of_sign " + Float.toString(number_of_sign));
			MessagesLog.d(TAG, "number_of_sign_rounded " + Integer.toString(number_of_sign_rounded));
			if (number_of_sign_rounded > 0) {
				for (int i = 0; i < Constants.FREQUENCIES.length; i++) {
					if (Constants.FREQUENCIES[i] + 20 > frequency
							&& Constants.FREQUENCIES[i] - 35 < frequency) {
						for (int j = 0; j < number_of_sign_rounded; j++) {
							sb.append(Constants.AVAILABLE_SIGNS.charAt(i));
						}

						return sb.toString();
					}
				}
			}
		}
		return Constants.NOEND_STR;
	}

}
