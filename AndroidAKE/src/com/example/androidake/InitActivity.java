package com.example.androidake;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.achartengine.GraphicalView;
import org.apache.commons.codec.DecoderException;

import org.apache.commons.codec.binary.Hex;

//import org.apache.commons.codec.binary.Base64;
import base64.Base64;
import com.example.graphic.LineGraph;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.SoundGenerator;
import com.example.important.Constants.STATUS;
import com.example.recorder.VoiceRecognition;

import Decoder.BASE64Decoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;


public class InitActivity extends Activity implements VoiceRecognition.Listener, SoundGenerator.Listener {
	private final static String TAG = "InitActivity";
	public MutualAuthenticateChip mac_A;
	public MutualAuthenticateChip mac_B;
	private STATUS status;
	private boolean initializator;
	private SoundGenerator soundgen;
	private VoiceRecognition voicerec;
	private TextView processes;
	private LinearLayout chart_container_fft;
	private static GraphicalView graph_view_fft;
	private LineGraph line_fft = new LineGraph(true);
	private StringBuilder sb;
	private StringBuilder ephemeral_key_B;
    private final static int MSG_SET_RECG_TEXT = 1;
    private final static int MSG_RECG_START = 2;
    private final static int MSG_RECG_END = 3;
    private boolean start_of_message = false;
    private Handler handler;
    private Button button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		prepareContainer();
		
		status = STATUS.NEUTRAL;
		
		boolean is_init = getIntent().getExtras().getBoolean(
				Constants.bundle_init_id);
		//initializator = is_init;
		setupActionBar();
		
		soundgen = new SoundGenerator(Constants.SAMPLING);
		soundgen.setListener(this);
		voicerec = new VoiceRecognition();
		voicerec.setListener(this);
		line_fft.setDecSubject(voicerec.getDecoder());
		button2 = (Button) findViewById(R.id.send_eph_key);
		
		sb = new StringBuilder();
		
		ephemeral_key_B = new StringBuilder();
		processes = (TextView) findViewById(R.id.processes);
		handler = new RegHandler(processes, sb);
		addMessageOnView("Nas³uchiwanie....");
		
		voicerec.start();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public void click(View view) throws UnsupportedEncodingException {
		voicerec.stop();
		initParty(true);
		process();
	}
	
	private void initParty(boolean init){
		initializator = init;
		mac_A = new MutualAuthenticateChip();
		mac_A.prepareMACCPP(init);
		changeStatus();
	}
	
	
	private void process() {
		
		String ephemeral_key_A;
		String ephemKey_base64;
		switch (status) {
			case INIT: 
				ephemeral_key_A = mac_A.getEphemeralKeyCPP(initializator);
				ephemKey_base64 = ConverterJava.fromHexStringToBase64(ephemeral_key_A);
				soundgen.setTextToEncode(ephemKey_base64);
				button2.setText("Ephemeral Key is sending... Please wait");
				button2.setEnabled(false);
	        	soundgen.start();
	        	break;
			case RECEIV_EPH:
				button2.setText("Receiving ephemeral key...");
				button2.setEnabled(false);
				if(initializator)
					voicerec.start();
				
				break;
			case SEND_EPHEM:
				voicerec.stop();
				button2.setText("Ephemeral Key is sending... Please wait");
				button2.setEnabled(false);
				String public_key_B = mac_A.getPublicKeyAnotherPartyCPP(initializator);
				String temp_ephemeral_key_B = ConverterJava.fromBase64StringToHex(ephemeral_key_B.toString());
				temp_ephemeral_key_B = temp_ephemeral_key_B + "H";
				mac_A.setEphemeralAndPublicKeyFromPartyCPP(initializator, temp_ephemeral_key_B, public_key_B);
				ephemeral_key_A = mac_A.getEphemeralKeyCPP(initializator);
				ephemKey_base64 = ConverterJava.fromHexStringToBase64(ephemeral_key_A);
				soundgen.setTextToEncode(ephemKey_base64);
				soundgen.start();
				break;
			default: break;
		}
	}

	
	public void test(View view) throws UnsupportedEncodingException {
		String ephemeralKey_A = mac_A.getEphemeralKeyCPP(true);
//		byte [] ephemeralKey_byte = ConverterJava.hexStringToByteArray(ephemeralKey_A.substring(0, ephemeralKey_A.length() - 1));
//		
//		addMessageOnView(new String(ephemeralKey_byte));
		
		String ephemKey_base64 = ConverterJava.fromHexStringToBase64(ephemeralKey_A);
		addMessageOnView(ephemeralKey_A);
		addMessageOnView(ephemKey_base64);
		ephemeralKey_A = ConverterJava.fromBase64StringToHex(ephemKey_base64);
		addMessageOnView(ephemeralKey_A);
		String ephemeralKey_B = mac_B.getEphemeralKeyCPP(false);
		ephemeralKey_A = ephemeralKey_A + "H";
		String publicKey_B = mac_A.getPublicKeyAnotherPartyCPP(true);
		String publicKey_A = mac_B.getPublicKeyAnotherPartyCPP(false);

		mac_A.setEphemeralAndPublicKeyFromPartyCPP(true, ephemeralKey_B, publicKey_B);
		mac_B.setEphemeralAndPublicKeyFromPartyCPP(false, ephemeralKey_A, publicKey_A);
		//
		byte[] cert_A = mac_A.prepareEncryptionCPP(true, true);

		String cipher_64 = Base64.encodeBytes(cert_A);
		addMessageOnView(cipher_64);
		byte[] chwila_prawdy = null;
		try {
			chwila_prawdy = Base64.decode(cipher_64);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean result = mac_B.decodeEncryption(false, chwila_prawdy);
		if (result == true) {
			addMessageOnView("Wow, jaja, dzia³a, nie wierze");
		} else {
			addMessageOnView("Nie jestem zdziwiony");
		}
		
		byte[] cert_B = mac_B.prepareEncryptionCPP(false, false);
		String cipher_64_B = Base64.encodeBytes(cert_B);
		addMessageOnView(cipher_64_B);
		byte[] chwila_prawdy_B = null;
		try {
			chwila_prawdy_B = Base64.decode(cipher_64_B);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean result_A = mac_A.decodeEncryption(true, chwila_prawdy_B);
		if (result_A == true) {
			addMessageOnView("Wow, jaja, dzia³a dla A, nie wierze");
		} else {
			addMessageOnView("Nie jestem zdziwiony");
		}
		
		processes.setText(sb.toString());

	}
	
	
	

//


	private void HideLayout(int iid) {
		LinearLayout myLayout = (LinearLayout) findViewById(iid);
		for (int i = 0; i < myLayout.getChildCount(); i++) {
			View view = myLayout.getChildAt(i);
			view.setVisibility(View.GONE); // Or whatever you want to do with
											// the view.
		}
	}

	private void changeStatus() {
		if (initializator) {
			switch (status) {
			case NEUTRAL:
				status = STATUS.INIT;
				break;
			case INIT:
				status = STATUS.RECEIV_EPH;
				break;
			case RECEIV_EPH:
				status = STATUS.SEND_ENC;
				break;
			case SEND_ENC:
				status = STATUS.RECEIV_ENC;
				break;
			case RECEIV_ENC:
				status = STATUS.GEN_SESSION_KEY;
				break;
			default:
				break;
			}
		} else {
			switch (status) {
			case NEUTRAL:
				status = STATUS.RECEIV_EPH;;
				break;
			case RECEIV_EPH:
				status = STATUS.SEND_EPHEM;
				break;
			case SEND_EPHEM:
				status = STATUS.RECEIV_ENC;
				break;
			case RECEIV_ENC:
				status = STATUS.SEND_ENC;
				break;
			case SEND_ENC:
				status = STATUS.GEN_SESSION_KEY;
				break;
			default:
				break;
			}
		}

	}

	private void prepareContainer() {
		chart_container_fft = (LinearLayout) findViewById(R.id.Chart_layout2);
		graph_view_fft = line_fft.getView(getBaseContext());
		chart_container_fft.addView(graph_view_fft);
	}

	private void addMessageOnView(String txt) {
		sb.append(txt);
		sb.append("\n");
		processes.setText(sb.toString());
	}
	
	private static class RegHandler extends Handler {
        public static StringBuilder mTextBuilder; // = new StringBuilder();
        private TextView mRecognisedTextView;

        public RegHandler(TextView textView, StringBuilder txtBuilder) {
            mRecognisedTextView = textView;
            mTextBuilder = txtBuilder;
        }
        
        public void setTxt(StringBuilder txt){
        	mTextBuilder = txt;
        }
        
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SET_RECG_TEXT:
                char ch = (char) msg.arg1;
                mTextBuilder.append(ch);
                if (null != mRecognisedTextView) {
                    mRecognisedTextView.setText(mTextBuilder.toString());
                }
                break;

            case MSG_RECG_START:
                mTextBuilder.delete(0, mTextBuilder.length());
                break;

            case MSG_RECG_END:
                MessagesLog.d(TAG, "recognition end");
                break;
            }
            super.handleMessage(msg);
        }
    }
	
	@Override
	public void onStartRecogntion() {

	}

	@Override
	public void onRecognition(String str) {
		MessagesLog.d(TAG, "Weszlo sobie do recognititon");
		if(str.length() > 0)
		if(start_of_message == false){
				if(str.charAt(0) == Constants.STANDARD_ALPHABET[64]){ //start
					start_of_message = true;
					if(status == STATUS.NEUTRAL){
						initParty(false);
						process();
					}
				}
		}else{
				if(str.charAt(0) == Constants.STANDARD_ALPHABET[64]){ //start
					
				}else if(str.charAt(0) == Constants.STANDARD_ALPHABET[65]){ //end data
					
					changeStatus();
					process();
				}else{
					ephemeral_key_B.append(str.charAt(0));
					Message msg = new Message();
					msg.obj = str;
					for(int i = 0 ; i < str.length(); i++){
						handler.sendMessage(handler.obtainMessage(MSG_SET_RECG_TEXT, str.charAt(i), 0));
					}
				}
		}
	}

	@Override
	public void onEndRecognition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void EndOfSending() {
		MessagesLog.d(TAG, "Weszlo sobie tutaj po zakonczeniu?");
		changeStatus();
		process();
	}

}
