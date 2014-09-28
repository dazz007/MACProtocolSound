package com.example.androidake;

import java.io.IOException;

import org.achartengine.GraphicalView;
import org.apache.commons.codec.DecoderException;

import org.apache.commons.codec.binary.Hex;

import org.apache.commons.codec.binary.Base64;

import com.example.graphic.LineGraph;
import com.example.important.Constants;
import com.example.important.Constants.STATUS;

import Decoder.BASE64Decoder;
import android.os.Bundle;
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

public class InitActivity extends Activity {
	public MutualAuthenticateChip mac_A;
	public MutualAuthenticateChip mac_B;
	private STATUS status;
	private boolean initializator;
	private TextView tv_key_gen_res;
	private TextView tv_ekey_gen_res;
	private TextView processes;
	private Button but_send_ephemeral_key;
	private LinearLayout chart_container_fft;
	private static GraphicalView graph_view_fft;
	private LineGraph line_fft = new LineGraph(true);
	private StringBuilder sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		prepareContainer();

		boolean is_init = getIntent().getExtras().getBoolean(
				Constants.bundle_init_id);
		initializator = is_init;
		setFirstStatus(is_init);

		// tv_key_gen_res = (TextView) findViewById(R.id.key_gen_res);
		// tv_ekey_gen_res = (TextView) findViewById(R.id.ekey_gen_res);
		// tv_key_gen_res.setText("Done");
		// tv_ekey_gen_res.setText("Done");
		// View b = findViewById(R.id.info_text);
		// b.setVisibility(View.GONE);
		// Show the Up button in the action bar.
		setupActionBar();
		sb = new StringBuilder();
		mac_A = new MutualAuthenticateChip();
		// mac.set_initializator(true);
		mac_A.prepareMACCPP(is_init);
		mac_B = new MutualAuthenticateChip();
		mac_B.prepareMACCPP(false);
		processes = (TextView) findViewById(R.id.processes);
		addMessageOnView("Zaraz sie zacznie magia");
		processes.setText("test test test");
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

	public void setFirstStatus(boolean init) {
		if (init) {
			status = STATUS.INIT;
		} else {
			status = STATUS.RECEIV_EPH;
		}
	}

	public void sendEphemeralKey(View view) {
		String ephemeralKey_A = mac_A.getEphemeralKeyCPP(true);
		String ephemeralKey_B = mac_B.getEphemeralKeyCPP(false);

		String publicKey_B = mac_A.getPublicKeyAnotherPartyCPP(true);
		String publicKey_A = mac_B.getPublicKeyAnotherPartyCPP(false);
		//
		addMessageOnView(ephemeralKey_A);
		addMessageOnView(ephemeralKey_B);
		addMessageOnView(publicKey_B);
		addMessageOnView(publicKey_A);
		// addMessageOnView(ephemeralKey_A);
		// String dupa = mac_A.getSomeString(true);
		// addMessageOnView("dupa "+dupa);
		// String ephemeralKey_A_str =
		// ConverterJava.ByteToString(ephemeralKey_A);
		// addMessageOnView(ephemeralKey_A_str);
		// String ephemeralKey_B_str =
		// ConverterJava.ByteToString(ephemeralKey_B);
		// addMessageOnView(ephemeralKey_B_str);
		// //
		// String publicKey_B_str = ConverterJava.ByteToString(publicKey_B);
		// addMessageOnView(publicKey_B_str);
		// String publicKey_A_str = ConverterJava.ByteToString(publicKey_A);
		// addMessageOnView(publicKey_A_str);
		// //
		mac_A.setEphemeralAndPublicKeyFromPartyCPP(true, ephemeralKey_B.substring(0, ephemeralKey_B.length()-1),
				publicKey_B.substring(0, publicKey_B.length()-1));
		mac_B.setEphemeralAndPublicKeyFromPartyCPP(false, ephemeralKey_A.substring(0, ephemeralKey_A.length()-1),
				publicKey_A.substring(0, publicKey_A.length()-1));
		//
		mac_A.prepareEncryptionCPP(true, true);
		String cipher_byte_a = mac_A.getEncryptCertAndRCPP(true);
		addMessageOnView(cipher_byte_a);
//		boolean result = mac_B.decodeEncryption(false, cipher_byte_a);
//		if (result == true) {
//			addMessageOnView("Wow, jaja, dzia³a, nie wierze");
//		} else {
//			addMessageOnView("Nie jestem zdziwiony");
//		}
		// //byte [] cipher_byte_b = mac_B.getEncryptCertAndRCPP(false);
		//
		// String cipher_byte_str_a = ConverterJava.ByteToString(cipher_byte_a);
		// addMessageOnView("Szyfr przed zamiana: "+cipher_byte_str_a);
		//
		// byte[] result = mac_B.decodeEncryption(false, cipher_byte_str_a);
		// String result_str = ConverterJava.ByteToString(result);
		// addMessageOnView("Szyfr po zamianie: "+result_str);

		// String cipher_byte_str_b = ConverterJava.ByteToString(cipher_byte_b);

		// mac_A

		// String str = "";
		//
		// addMessageOnView("dlugosc efemerycznego: "+ephemeralKey.length);
		// str = ConverterJava.ByteToString(ephemeralKey);
		// addMessageOnView("efemeryczy normlany: ");
		// addMessageOnView(str);
		//
		// byte [] base64 = fromHexToBase64(ephemeralKey);
		// str = ConverterJava.ByteToString(base64);
		// addMessageOnView(str);
		// BASE64Decoder decoder = new BASE64Decoder();
		// byte[] imageByte = null;
		// try {
		// imageByte = decoder.decodeBuffer(str);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// addMessageOnView("dupa blada kurwa2");
		// e.printStackTrace();
		// }
		// addMessageOnView("rozmiar kurwy" + imageByte.length);
		// String kurwa = ConverterJava.ByteToString(imageByte);
		// addMessageOnView(kurwa);
		// byte[] encodedHexB64 = Base64.encodeBase64(imageByte);
		//
		// addMessageOnView("serio? "+ConverterJava.ByteToString(encodedHexB64));
		processes.setText(sb.toString());

	}

	private byte[] fromHexToBase64(byte[] hex_byte) {
		String hex_str = ConverterJava.ByteToString(hex_byte);
		byte[] decodedHex = null;
		try {
			decodedHex = Hex.decodeHex(hex_str.substring(0,
					hex_str.length() - 1).toCharArray());
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] encodedHexB64 = Base64.encodeBase64(decodedHex);

		return encodedHexB64;
	}

	private String fromBase64ToHex(byte[] base64_byte) {
		byte[] decodedB64Hex = Base64.decodeBase64(base64_byte);
		return ConverterJava.bytesToHex(decodedB64Hex);
	}

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

}
