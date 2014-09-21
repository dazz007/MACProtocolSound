package com.example.important;

public class Buffer {
	public byte[] buffer;
	private int buffer_size;
	public int [] buffer_values;
	private int buffer_values_size;
	public short[] buffer_short;
	private int buffer_size_short;
	public float[] buffer_fft;
	private int buffer_size_fft;
	private long time;

	public Buffer(){
		
	}

//	public Buffer(int maxSize) {
//		buffer = new byte[maxSize];
//	}

//	
//	public Buffer(byte[] buf, int buf_size, int[] buff_values, int buff_values_size) {
//		buffer = buf;
//		buffer_size = buf_size;
//		setBufferValues(buff_values);
//		setBufferValuesSize(buff_values_size);
//	}

	public void setBuffer(byte[] buf) {
		buffer = buf;
	}

	public void setBufferSize(int buf_size) {
		buffer_size = buf_size;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public int getBufferSize() {

		return buffer_size;
	}

	public int [] getBufferValues() {
		return buffer_values;
	}

	public void setBufferValues(int [] buffer_values) {
		this.buffer_values = buffer_values;
	}

	public int getBufferValuesSize() {
		return buffer_values_size;
	}

	public void setBufferValuesSize(int buffer_values_size) {
//		this.buffer_values = new int[buffer_values_size];
		this.buffer_values_size = buffer_values_size;
	}
	
	public short[] getBufferShort() {
		return buffer_short;
	}

	public void setBufferShort(short[] buffer_short) {
		this.buffer_short = buffer_short;
	}

	public int getBufferSizeShort() {
		return buffer_size_short;
	}

	public void setBufferSizeShort(int buffer_size_short) {
		this.buffer_size_short = buffer_size_short;
	}
	
	public void initializeBufferByte(int biffer_size){
		this.buffer = new byte[biffer_size];
	}
	
	public void initializeBufferShort(int buffer_size){
		this.buffer_short = new short[buffer_size];
	}
	
	public void initializeBufferInt(int buffer_size){
		this.buffer_values = new int[buffer_size];
	}
	
	public float[] getBufferFFT() {
		return buffer_fft;
	}

	public void setBufferFFT(float[] buffer_fft) {
		this.buffer_fft = buffer_fft;
	}

	public int getBuffer_size_fft() {
		return buffer_size_fft;
	}

	public void setBuffer_size_fft(int buffer_size_fft) {
		this.buffer_size_fft = buffer_size_fft;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
