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
	public final static int BORDER = 50;
	public final static int AMPLITUDE_LT_14K = 50;
	public final static int AMPLITUDE_GT_14K = 10;
	public static boolean DRAW_IN_TIME = true;
	public static boolean DRAW_FFT = false;
	public static int ULTRASOUND = 1;
	
	
	public static int[] FREQUENCIES = { 	10000, // A
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
											19850, // .
										};
	
	public static int[] FREQUENCIES_150 = { 	10500,
		10650,
		10800,
		10950,
		11100,
		11250,
		11400,
		11550,
		11700,
		11850,
		12000,
		12150,
		12300,
		12450,
		12600,
		12750,
		12900,
		13050,
		13200,
		13350,
		13500,
		13650,
		13800,
		13950,
		14100,
		14250,
		14400,
		14550,
		14700,
		14850,
		15000,
		15150,
		15300,
		15450,
		15600,
		15750,
		15900,
		16050,
		16200,
		16350,
		16500,
		16650,
		16800,
		16950,
		17100,
		17250,
		17400,
		17550,
		17700,
		17850,
		18000,
		18150,
		18300,
		18450,
		18600,
		18750,
		18900,
		19050,
		19200,
		19350,
		19500,
		19650,
		19800,
		19950,
		20100,
		20250
	};
	public static int[] FREQUENCIES_SPACE_100 = { 
														13800, // A
														13900, // B
														14000, // C
														14100, // D
														14200, // E
														14300, // F
														14400, // G
														14500, // H
														14600, // I
														14700, // J
														14800, // K
														14900, // L
														15000, // M
														15100, // N
														15200, // O
														15300, // P
														15400, // Q
														15500, // R
														15600, // S
														15700, // T
														15800, // U
														15900, // V
														16000, // W
														16100, // X
														16200, // Y
														16300, // Z
														16400, // a
														16500, // b
														16600, // c
														16700, // d
														16800, // e
														16900, // f
														17000, // g
														17100, // h
														17200, // i
														17300, // j
														17400, // k
														17500, // l
														17600, // m
														17700, // n
														17800, // o
														17900, // p
														18000, // q
														18100, // r
														18200, // s
														18300, // t
														18400, // u
														18500, // v
														18600, // w
														18700, // x
														18800, // y
														18900, // z
														19000, // 0
														19100, // 1
														19200, // 2
														19300, // 3
														19400, // 4
														19500, // 5
														19600, // 6
														19700, // 7
														19800, // 8
														19900, // 9
														20000, // +
														20100, // /
														20200, // ,
														20300, // .
														};
	
	public static int[] FREQUENCIES_SPACE_130 = {  11800,
		11930,
		12060,
		12190,
		12320,
		12450,
		12580,
		12710,
		12840,
		12970,
		13100,
		13230,
		13360,
		13490,
		13620,
		13750,
		13880,
		14010,
		14140,
		14270,
		14400,
		14530,
		14660,
		14790,
		14920,
		15050,
		15180,
		15310,
		15440,
		15570,
		15700,
		15830,
		15960,
		16090,
		16220,
		16350,
		16480,
		16610,
		16740,
		16870,
		17000,
		17130,
		17260,
		17390,
		17520,
		17650,
		17780,
		17910,
		18040,
		18170,
		18300,
		18430,
		18560,
		18690,
		18820,
		18950,
		19080,
		19210,
		19340,
		19470,
		19600,
		19730,
		19860,
		19990,
		20120,
		20250
														};
	
	public static int[] FREQUENCIES_SPACE_100_2 = { 
	14000,
	14100,
	14200,
	14300,
	14400,
	14500,
	14600,
	14700,
	14800,
	14900,
	15000,
	15100,
	15200,
	15300,
	15400,
	15500,
	15600,
	15700,
	15800,
	15900,
	16000,
	16100,
	16200,
	16300,
	16400,
	16500,
	16600,
	16700,
	16800,
	16900,
	17000,
	17100,
	17200,
	17300,
	17400,
	17500,
	17600,
	17700,
	17800,
	17900,
	18000,
	18100,
	18200,
	18300,
	18400,
	18500,
	18600,
	18700,
	18800,
	18900,
	19000,
	19100,
	19200,
	19300,
	19400,
	19500,
	19600,
	19700,
	19800,
	19900,
	20000,
	20100,
	20200,
	20300,
	20400,
	20500
};
	
	
	public static int[] FREQUENCIES_SPACE_100_3 = { 
	14300,
	14400,
	14500,
	14600,
	14700,
	14800,
	14900,
	15000,
	15100,
	15200,
	15300,
	15400,
	15500,
	15600,
	15700,
	15800,
	15900,
	16000,
	16100,
	16200,
	16300,
	16400,
	16500,
	16600,
	16700,
	16800,
	16900,
	17000,
	17100,
	17200,
	17300,
	17400,
	17500,
	17600,
	17700,
	17800,
	17900,
	18000,
	18100,
	18200,
	18300,
	18400,
	18500,
	18600,
	18700,
	18800,
	18900,
	19000,
	19100,
	19200,
	19300,
	19400,
	19500,
	19600,
	19700,
	19800,
	19900,
	20000,
	20100,
	20200,
	20300,
	20400,
	20500,
	20600,
	20700,
	20800
};
	
	
	
//	 Other thing
	public final static String bundle_init_id = "is_initializator";
	
	public static enum STATUS { NEUTRAL, INIT, RECEIV_EPH, SEND_EPHEM, SEND_ENC, RECEIV_ENC, GEN_SESSION_KEY };
	
	public static enum RANGE { RA_150, RA_130, RA_100, RA_100_2, RA_100_3 };
	
}
