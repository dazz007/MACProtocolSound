package com.example.androidake;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ConverterJava {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
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
	
	static String Base64ToString(byte [] data){
		String s = "";
		s = new String(data);
		return s;
	}
	
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
