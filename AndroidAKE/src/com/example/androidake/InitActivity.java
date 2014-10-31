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
import android.graphics.LightingColorFilter;
import android.os.Build;

public class InitActivity extends Activity implements
		VoiceRecognition.Listener, SoundGenerator.Listener {
	private final static String TAG = "InitActivity";
	public MutualAuthenticateChip mac_A;
	public MutualAuthenticateChip mac_B;
	private static STATUS status;
	private boolean initializator;
	private SoundGenerator soundgen;
	private VoiceRecognition voicerec;
	private TextView processes;
	private LinearLayout chart_container_fft;
	private static GraphicalView graph_view_fft;
	private LineGraph line_fft = new LineGraph(false);
	private StringBuilder sb;
	private StringBuilder epehemral_key_or_encryption;
	private final static int MSG_SET_RECG_TEXT = 1;
	private final static int MSG_RECG_START = 2;
	private final static int MSG_RECG_END = 3;
	private final static int MSG_RECG_CORRECT = 4;
	private final static int MSG_RECG_WRONG = 5;
	private final static int SHOW_IT = 6;
	private final static int MSG_SEND_EPH = 7;
	private final static int MSG_SEND_ENC = 8;
	private boolean start_of_message = false;
	private Handler handler;
	private Button button2;
	private boolean works;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		prepareContainer();

		status = STATUS.NEUTRAL;

		boolean is_init = getIntent().getExtras().getBoolean(
				Constants.bundle_init_id);
		// initializator = is_init;
		setupActionBar();
		works = false;
		soundgen = new SoundGenerator(Constants.SAMPLING);
		soundgen.setListener(this);
		voicerec = new VoiceRecognition();
		voicerec.setListener(this);
		line_fft.setSubject(voicerec);
	    voicerec.register(line_fft);
		line_fft.start(false);
		
		sb = new StringBuilder();

		epehemral_key_or_encryption = new StringBuilder();
		processes = (TextView) findViewById(R.id.processes);
		TextView session_key_txt = (TextView) findViewById(R.id.text_session_key);
		handler = new RegHandler(processes, sb, button2, session_key_txt);
		addMessageOnView("Waiting for initializator...\n");
		
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

	public void click(View view) throws UnsupportedEncodingException, InterruptedException {
		if(status == Constants.STATUS.GEN_SESSION_KEY){
			start_of_message = false;
			soundgen.setStopStatus();
			voicerec.stop();
			
			finish();
			startActivity(getIntent());
		}else{
			voicerec.stop();
			initParty(true);
			process();
		}
		
	}

	private void initParty(boolean init) {
		initializator = init;
		mac_A = new MutualAuthenticateChip();
		mac_A.prepareMACCPP(init);
		changeStatus();
	}

	private void process() throws InterruptedException {
		String ephemKey_base64;
		
		switch (status) {
		case INIT:
			ephemKey_base64 = getEphemeralKey();
			//addToHandler(ephemKey_base64);
			handler.sendEmptyMessage(MSG_SEND_EPH);
			soundgen.setTextToEncode(ephemKey_base64);
			soundgen.start();
			break;
		case RECEIV_EPH:
			if (initializator)
				voicerec.start();
			break;
		case SEND_EPHEM:
			voicerec.stop();
			setEphemeralKeyAnotheParty(epehemral_key_or_encryption.toString());
			ephemKey_base64 = getEphemeralKey();
			//addToHandler(ephemKey_base64);
			handler.sendEmptyMessage(MSG_SEND_EPH);
			soundgen.setTextToEncode(ephemKey_base64);
			soundgen.start();
			break;
		case SEND_ENC:
			voicerec.stop();
			if(initializator){
				setEphemeralKeyAnotheParty(epehemral_key_or_encryption.toString());
			}else{
				works = decryptEncryption(epehemral_key_or_encryption.toString());
			}
			String cipher_64 = "";
			
			if(initializator == false && works == false){
				cipher_64 = "ABCDEFGHIJK";
			}else{
				cipher_64 = getEncryption();
			}
			//addToHandler(cipher_64);
			handler.sendEmptyMessage(MSG_SEND_ENC);
			soundgen.setTextToEncode(cipher_64);
			soundgen.start();
			break;
		case RECEIV_ENC:
			voicerec.start();
			break;
		case GEN_SESSION_KEY:
			if(initializator){
				works = decryptEncryption(epehemral_key_or_encryption.toString());
			}
			if(works == true){
				
				handler.sendEmptyMessage(MSG_RECG_CORRECT);
				addToHandler(mac_A.getSessionKeyCPP(initializator));
			}else{
				handler.sendEmptyMessage(MSG_RECG_WRONG);
			}
			//changeStatus();
			break;
		default:
			break;
		}
	}
	
	private String getEncryption(){
		byte[] cert_A = mac_A.prepareEncryptionCPP(initializator, initializator);
		return Base64.encodeBytes(cert_A);
	}
	private boolean decryptEncryption(String encryption){
		String enc = encryption + "==";
		byte[] chwila_prawdy = null;
		try {
			chwila_prawdy = Base64.decode(enc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mac_A.decodeEncryption(initializator, chwila_prawdy);
	}
	
	//
	private void setEphemeralKeyAnotheParty(String ephemer2){
		String eph = ephemer2 + "=";
		String public_key_B = mac_A
				.getPublicKeyAnotherPartyCPP(initializator);
		String temp_ephemeral_key_B = ConverterJava
				.fromBase64StringToHex(eph);
		temp_ephemeral_key_B = temp_ephemeral_key_B + "H";
		mac_A.setEphemeralAndPublicKeyFromPartyCPP(initializator, temp_ephemeral_key_B, public_key_B);
	}
	
	private void addToHandler(String str){
		Bundle bundle = new Bundle();
		bundle.putString(null, str);
		Message msg = new Message();
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	
	private String getEphemeralKey(){
		String ephemeral_key_A = mac_A.getEphemeralKeyCPP(initializator);
		return ConverterJava
				.fromHexStringToBase64(ephemeral_key_A);
	}
	private void changeStatus() {
		if (initializator) {
			switch (status) {
			case NEUTRAL:
				MessagesLog.d(TAG, "INIT");
				status = STATUS.INIT;
				break;
			case INIT:
				MessagesLog.d(TAG, "RECEIV_EPH");
				status = STATUS.RECEIV_EPH;
				break;
			case RECEIV_EPH:
				MessagesLog.d(TAG, "SEND_ENC");
				status = STATUS.SEND_ENC;
				break;
			case SEND_ENC:
				MessagesLog.d(TAG, "RECEIV_ENC");
				status = STATUS.RECEIV_ENC;
				break;
			case RECEIV_ENC:
				MessagesLog.d(TAG, "GEN_SESSION_KEY");
				status = STATUS.GEN_SESSION_KEY;
				break;
			case GEN_SESSION_KEY:
				MessagesLog.d(TAG, "NEUTRAL");
				status = STATUS.NEUTRAL;
				break;
			default:
				break;
			}
		} else {
			switch (status) {
			case NEUTRAL:
				MessagesLog.d(TAG, "RECEIV_EPH");
				status = STATUS.RECEIV_EPH;
				break;
			case RECEIV_EPH:
				MessagesLog.d(TAG, "SEND_EPHEM");
				status = STATUS.SEND_EPHEM;
				break;
			case SEND_EPHEM:
				MessagesLog.d(TAG, "RECEIV_ENC");
				status = STATUS.RECEIV_ENC;
				break;
			case RECEIV_ENC:
				MessagesLog.d(TAG, "SEND_ENC");
				status = STATUS.SEND_ENC;
				break;
			case SEND_ENC:
				MessagesLog.d(TAG, "GEN_SESSION_KEY");
				status = STATUS.GEN_SESSION_KEY;
				break;
			case GEN_SESSION_KEY:
				MessagesLog.d(TAG, "NEUTRAL");
				status = STATUS.NEUTRAL;
				break;
			default:
				break;
			}
		}

	}
	
	public void onBackPressed() {
		soundgen.setStopStatus();
		try {
			voicerec.stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.finish();
	}
	
	private void prepareContainer() {
		button2 = (Button) findViewById(R.id.send_eph_key);
		chart_container_fft = (LinearLayout) findViewById(R.id.Chart_layout2);
		graph_view_fft = line_fft.getView(getBaseContext());
		chart_container_fft.addView(graph_view_fft);
		
	}

	private void addMessageOnView(String txt) {
		sb.append(txt);
		//sb.append("\n");
		processes.setText(sb.toString());
	}

	private static class RegHandler extends Handler {
		public static StringBuilder m_text_builder; // = new StringBuilder();
		private TextView m_recognised_text_view;
		private Button m_button;
		private TextView m_session_key_text;
		public RegHandler(TextView textView, StringBuilder txtBuilder,
				Button btn, TextView session_Key_txt) {
			m_recognised_text_view = textView;
			m_text_builder = txtBuilder;
			m_button = btn;
			m_session_key_text = session_Key_txt;
		}

		public void setTxt(StringBuilder txt) {
			m_text_builder = txt;
		}

		public void setButton(Button button) {
			//button.setText("Protocol is in process...");
			switch (status) {
			case INIT:
				button.setText("Protocol is in process...");
				//button.setText("Ephemeral Key is sending... Please wait");
				button.setEnabled(false);
				break;
			case RECEIV_EPH:
				button.setText("Protocol is in process...");
				//button.setText("Receiving ephemeral key...");
				button.setEnabled(false);
				break;
			case SEND_EPHEM:
				button.setText("Protocol is in process...");
				//button.setText("Ephemeral Key is sending... Please wait");
				button.setEnabled(false);
				break;
			case SEND_ENC:
				button.setText("Protocol is in process...");
				//button.setText("Encryption is sending... Please wait");
				button.setEnabled(false);
				break;
			case RECEIV_ENC:
				button.setText("Protocol is in process...");
				//button.setText("Receiving encryption...");
				button.setEnabled(false);
				break;
			default:
				break;
			}
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SET_RECG_TEXT:
				char ch = (char) msg.arg1;
				m_text_builder.append(ch);
				if (null != m_recognised_text_view) {
					//m_recognised_text_view.setText(m_recognised_text_view.getText() + Character.toString(ch));
				}
				break;

			case MSG_RECG_START:
				setButton(m_button);
				m_text_builder.delete(0, m_text_builder.length());
				if(status == Constants.STATUS.RECEIV_EPH)
					m_recognised_text_view.setText(m_recognised_text_view.getText() + "Receiving ephemeral key...\n");
				else if(status == Constants.STATUS.RECEIV_ENC)
					m_recognised_text_view.setText(m_recognised_text_view.getText() + "Receiving encryption...\n");
				//m_recognised_text_view.setText("");
				break;

			case MSG_RECG_END:
				//m_text_builder.append(" Rozmiar: "+m_text_builder.toString().length());
//				if(status == Constants.STATUS.INIT || status == Constants.STATUS.NEUTRAL){
//					m_recognised_text_view.setText(m_recognised_text_view.getText() + "Ephemeral key is received. Size of ephemeral key: "+m_text_builder.toString().length()+"\n");
//				}else if(status == Constants.STATUS.RECEIV_EPH || status == Constants.STATUS.RECEIV_ENC){
					m_recognised_text_view.setText(m_recognised_text_view.getText() + "Size of message: "+m_text_builder.toString().length()+"\n");

				//setButton(m_button);
				MessagesLog.d(TAG, "recognition end");
				break;
			case MSG_RECG_CORRECT:
				m_button.getBackground().setColorFilter(new LightingColorFilter(0xFF00FF00, 0xFFAA0000));
				m_button.setText("Session key is established! Push button to try again.");
				m_button.setEnabled(true);
				break;
			case MSG_RECG_WRONG:
				m_button.getBackground().setColorFilter(new LightingColorFilter(0xFFFF0000, 0xFFAA0000));
				m_button.setText("Something has gone wrong! Push button to try again.");
				m_button.setEnabled(true);
				break;
			case MSG_SEND_EPH:
				m_recognised_text_view.setText(m_recognised_text_view.getText() + "Ephemeral key is sending.\n");
				break;
			case MSG_SEND_ENC:
				m_recognised_text_view.setText(m_recognised_text_view.getText() + "Encryption is sending.\n");
				break;
			default:

				String message = msg.getData().getString(null);
//				m_session_key_text.setText(m_recognised_text_view.getText() + "\n----------------------:\n");
				m_session_key_text.setText( message );
				break;
			}
			super.handleMessage(msg);
		}
	}

	@Override
	public void onStartRecogntion() {
		handler.sendEmptyMessage(MSG_RECG_START);
	}

	@Override
	public void onRecognition(String str) {
		if (str.length() > 0)
			if (start_of_message == false) {
				if (str.charAt(0) == Constants.STANDARD_ALPHABET[Constants.STANDARD_ALPHABET.length - 2]) { // start
					start_of_message = true;
					epehemral_key_or_encryption.delete(0, epehemral_key_or_encryption.length());
					if (status == STATUS.NEUTRAL) {
						initParty(false);
						onStartRecogntion();
					}
				}
			} else {
				if (str.charAt(0) == Constants.STANDARD_ALPHABET[Constants.STANDARD_ALPHABET.length - 2]) { // start

				} else if (str.charAt(0) == Constants.STANDARD_ALPHABET[Constants.STANDARD_ALPHABET.length - 1]) { // end data																				
					start_of_message = false;
					onEndRecognition();
					changeStatus();
					try {
						process();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} else {
					epehemral_key_or_encryption.append(str.charAt(0));
					Message msg = new Message();
					msg.obj = str;
						handler.sendMessage(handler.obtainMessage(
								MSG_SET_RECG_TEXT, str.charAt(0), 0));
				}
			}
	}

	@Override
	public void onEndRecognition() {
		handler.sendEmptyMessage(MSG_RECG_END);

	}

	@Override
	public void EndOfSending() {
		MessagesLog.d(TAG, "Weszlo sobie tutaj po zakonczeniu?");
		changeStatus();
		try {
			process();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void click2(View view) throws UnsupportedEncodingException {
		mac_A = new MutualAuthenticateChip();
		mac_B = new MutualAuthenticateChip();
		
		mac_A.prepareMACCPP(true);
		mac_B.prepareMACCPP(false);
		String ephemeralKey_A = mac_A.getEphemeralKeyCPP(true);

		String ephemKey_base64 = ConverterJava
				.fromHexStringToBase64(ephemeralKey_A);
		addMessageOnView(ephemKey_base64 + "\n"+"-------------------\n"+" Rozmiar: "+ephemKey_base64.length()+"\n");
		ephemeralKey_A = ConverterJava.fromBase64StringToHex(ephemKey_base64);
		ephemeralKey_A = ephemeralKey_A + "H";
		addMessageOnView(ephemeralKey_A + "\n"+"======================\n");
		
		String ephemeralKey_B = mac_B.getEphemeralKeyCPP(false);
		ephemKey_base64 = ConverterJava
				.fromHexStringToBase64(ephemeralKey_B);
		addMessageOnView(ephemKey_base64 + "\n"+"-------------------\n"+" Rozmiar: "+ephemKey_base64.length()+"\n");
		ephemeralKey_B = ConverterJava.fromBase64StringToHex(ephemKey_base64);
		ephemeralKey_B = ephemeralKey_B + "H";
		addMessageOnView(ephemeralKey_B + "\n"+"======================\n");
		
		String publicKey_B = mac_A.getPublicKeyAnotherPartyCPP(true);
		String publicKey_A = mac_B.getPublicKeyAnotherPartyCPP(false);

		mac_A.setEphemeralAndPublicKeyFromPartyCPP(true, ephemeralKey_B,
				publicKey_B);
		mac_B.setEphemeralAndPublicKeyFromPartyCPP(false, ephemeralKey_A,
				publicKey_A);
		//
		byte[] cert_A = mac_A.prepareEncryptionCPP(true, true);

		String cipher_64 = Base64.encodeBytes(cert_A);
		addMessageOnView(cipher_64+ "\n"+"-------------------\n"+" Rozmiar: "+cipher_64.length()+"\n");
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
		addMessageOnView(cipher_64_B+ "\n"+"-------------------\n"+" Rozmiar: "+cipher_64_B.length()+"\n");
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
}
