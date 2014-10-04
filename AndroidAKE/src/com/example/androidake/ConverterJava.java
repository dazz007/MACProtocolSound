package com.example.androidake;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import base64.Base64;

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
	
	static String ByteToStringUTF16(byte[] data){
		String s = "";
			try {
				s = new String(data, "UTF-16");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
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
	
	static byte[] StringToByteUTF16(String data){
		byte[] b = null;
		try {
			b = data.getBytes("UTF-16");
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
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static String fromHexToBase64(byte[] hex_byte) {
		String hex_str = ConverterJava.ByteToString(hex_byte);
		byte[] decodedHex = null;
		try {
//			decodedHex = Hex.decodeHex(hex_str.toCharArray());
			decodedHex = Hex.decodeHex(hex_str.substring(0,
					hex_str.length() - 1).toCharArray());
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String encodedHexB64 = Base64.encodeBytes(decodedHex);

		return encodedHexB64;
	}
	
	public static String fromHexStringToBase64(String text){
		String hex_byte = fromHexToBase64(text.getBytes());
//		byte [] hex_byte = hexStringToByteArray(text);
//		return fromHexToBase64(hex_byte);
		return hex_byte;
	}
	
	public static String fromBase64StringToHex(String base64_str) {
		byte[] base64_byte = null;
		try {
			base64_byte = base64_str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] decodedB64Hex = null;
		try {
			decodedB64Hex = Base64.decode(base64_byte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ConverterJava.bytesToHex(decodedB64Hex);
	}
	
	
}
