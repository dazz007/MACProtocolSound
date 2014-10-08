package com.example.important;

public class Constants {
	public final static int SAMPLING = 44100;
	public static final int BITS_16 = 32768;
	public final static int START_RECOGNITION = 2;
	public final static int STOP_RECOGNITION = 3;
	public final static int START_STATE = 1;
	public final static int STOP_STATE = 0;
	
	public final static char[] STANDARD_ALPHABET = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 
        'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 
        'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', 
        '6', '7', '8', '9', '+', '/', ',', '.'
    };
	
//	public final static String AVAILABLE_SIGNS = "0123456789ABCDEFHG";
//	public final static int DEFAULT_BUFFER_SIZE = 2*8192;
	public final static int DEFAULT_BUFFER_COUNT = 3;
	public final static int DEFAULT_GEN_DURATION = 30;
	public final static int DEFAULT_NUM_SAMPLES = DEFAULT_GEN_DURATION * SAMPLING / 1000;
	public final static int DEFAULT_BUFFER_SIZE = 8192;
	public final static int DEFAULT_BUFFER_SIZE_REC = 2048;
	public final static String START_OF_DATA = ",,";
	public final static String END_OF_DATA = "..";
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
//	public final static int[] FREQUENCIES = { 
//											  14000, //0
//											  14100, //1
//											  14250, //2
//											  14400, //3
//											  14550, //4
//											  18200, //5
//											  18350, //6
//											  18500, //7
//											  18650, //8
//											  18800, //9
//											  19000, //A
//											  19200, //B
//											  19300,// C
//											  19450,// D
//											  19600,// E
//											  20000,// F
//											  20200,// H
//											  20300 // G
//											  };
	
	public final static int[] FREQUENCIES = { 
	10000, // A
	10150, // B
	10300, // C
	10450, // D
	10600, // E
	10750, // F
	10900, // G
	11050, // H
	11200, // I
	11350, // J
	11500, // K
	11650, // L
	11800, // M
	11950, // N
	12100, // O
	12250, // P
	12400, // Q
	12550, // R
	12700, // S
	12850, // T
	13000, // U
	13150, // V
	13300, // W
	13450, // X
	13600, // Y
	13750, // Z
	13900, // a
	14050, // b
	14200, // c
	14350, // d
	14500, // e
	14750, // f
	14900, // g
	15050, // h
	15200, // i
	15350, // j
	15500, // k
	15650, // l
	15800, // m
	15950, // n
	16100, // o
	16250, // p
	16400, // q
	16550, // r
	16700, // s
	16850, // t
	17000, // u
	17150, // v
	17300, // w
	17450, // x
	17600, // y
	17750, // z
	17900, // 0
	18050, // 1
	18200, // 2
	18350, // 3
	18500, // 4
	18650, // 5
	18800, // 6
	18950, // 7
	19100, // 8
	19250, // 9
	19400, // +
	19550, // /
	19700, // ,
	19800, // .
	};
	
//	public final static int[] FREQUENCIES = { 10000, 10100, 10250, 10400, 10550,
//		11200, 11350, 11500, 11650, 11800, 12000, 12200, 12300, 12450, 12600, 13000, 13200, 13300 };
	
	
	
	
//	 Other thing
	public final static String bundle_init_id = "is_initializator";
	
	public static enum STATUS { NEUTRAL, INIT, RECEIV_EPH, SEND_EPHEM, SEND_ENC, RECEIV_ENC, GEN_SESSION_KEY };
	
}
