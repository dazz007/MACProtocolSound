package com.example.important;

public class Constants {
	public final static int SAMPLING = 44100;
	public static final int BITS_16 = 32768;
	public final static int START_RECOGNITION = 2;
	public final static int STOP_RECOGNITION = 3;
	public final static int START_STATE = 1;
	public final static int STOP_STATE = 0;
	public final static String AVAILABLE_SIGNS = "0123456789ABCDEFHG";
//	public final static int DEFAULT_BUFFER_SIZE = 2*8192;
	public final static int DEFAULT_BUFFER_COUNT = 3;
	public final static int DEFAULT_GEN_DURATION = 100;
	public final static int DEFAULT_NUM_SAMPLES = DEFAULT_GEN_DURATION * SAMPLING / 1000;
	public final static int DEFAULT_BUFFER_SIZE = 8192;
	public final static int DEFAULT_BUFFER_SIZE_REC = 2048;
	public final static String START_OF_DATA = "GH";
	public final static String END_OF_DATA = "HG";
	public final static char NOEND = '-';
	public final static String NOEND_STR = "-";
	// 0123456789ABCDEFGH = 15 signs
	
	public static boolean DRAW_IN_TIME = false;
	public static boolean DRAW_FFT = true;
	public static int ULTRASOUND = 1;
	
//	public final static int[] FREQUENCIES = { 
//											  1422, //0
//											  1575, //1
//											  1679, //2
//											  1803, //3
//											  2321, //4
//											  2670, //5
//											  2974, //6
//											  3103, //7
//											  3400, //8
//											  3609, //9
//											  3845, //A
//											  4410, //B
//											  4700, //C
//											  4923, //D
//											  5200, //E
//											  6400, //F
//											  7310, //G
//											  8210  //H  
//											  };
	public final static int[] FREQUENCIES = { 
											  14000, //0
											  14100, //1
											  14250, //2
											  14400, //3
											  14550,//4
											  18200, //5
											  18350, //6
											  18500, //7
											  18650, //8
											  18800, //9
											  19000, //A
											  19200, //B
											  19300,// C
											  19450,// D
											  19600,// E
											  20000,// F
											  20200,// G
											  20300 // H
											  };
//	public final static int[] FREQUENCIES = { 10000, 10100, 10250, 10400, 10550,
//		11200, 11350, 11500, 11650, 11800, 12000, 12200, 12300, 12450, 12600, 13000, 13200, 13300 };
}
