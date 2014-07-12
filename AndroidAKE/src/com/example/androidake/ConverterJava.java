package com.example.androidake;

import java.io.UnsupportedEncodingException;

public class ConverterJava {
	
	static String ByteToString(byte[] data){
		String s = "";
		try {
			s = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	static byte[] StringToByte(String data){
		byte[] b = null;
		try {
			b = data.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
}
