package com.example.androidake;

public class MutualAuthenticateChip {
	
	public boolean initiated = false;
	static {
		System.loadLibrary("nativecryptowrapper");
	}
	
	public native void prepareMACCPP(boolean init);

//	public native byte[] getEphemeralKeyCPP(boolean init);
	public native String getEphemeralKeyCPP(boolean init);
	
	public native String showKeyPair(boolean init);
	
	public native byte[] showPrivateKey(boolean init);
	
	public native String getPublicKeyAnotherPartyCPP(boolean init);
	
	public native void setEphemeralAndPublicKeyFromPartyCPP(boolean init, String ephemeralKey, String publicKey);
	
	public native void setEphemeralKeyFromPartyCPP(boolean init, String ephemeralKey);
	
	public native void prepareEncryptionCPP(boolean init, boolean initiated);
	
	public native String getEncryptCertAndRCPP(boolean init);
	
	public native void setEncryptionFromPartyCPP(boolean init, byte [] encryption );
	
	public native String getSomeString(boolean init);
	
	public native boolean decodeEncryption(boolean init, String cipher);
//	public native byte[] decodeEncryption(boolean init, String cipher);
	
	public native boolean verifCertificateCPP(boolean init);
	
	public native void computeSessionKeyCPP(boolean init);
	
	public native byte[] getSessionKeyCPP(boolean init);
	
	public native void setInitializatorCPP(boolean init);
	
	//-------------------------------
	
	
	
	
	public void set_initializator(boolean init){
		this.initiated = init;
		//setInitializatorCPP(init);
	}
	
//	public void prepare_keys_and_cert(){
//		prepareMACCPP();
//	}
	
//	public byte[] return_ephemeralkey(){
//		return getEphemeralKeyCPP();
//	}
//	
//	public void set_ephemeralkey_from_party(String ephemeralKey){
//		setEphemeralKeyFromPartyCPP(ephemeralKey);
//	}
//	
//	public byte[] get_encrypt_cert_and_R(){
//		return getEncryptCertAndRCPP();
//	}
//	
//	public void prepare_encryption(boolean initiated){
//		prepareEncryptionCPP(this.initiated);
//	}
//	
//	
//	public void set_encryption_from_party( byte [] encryption ){
//		setEncryptionFromPartyCPP(encryption);
//	}
//	
//	public boolean verif_certificate(){
//		return verifCertificateCPP();
//	}
//	
//	public void compute_session_key(){
//		computeSessionKeyCPP();
//	}
//	
//	public void get_session_key(){
//		byte[] sk = getSessionKeyCPP();
//	}
//	
//	public byte[] show_key_pair(){
//		return showKeyPair();
//	}
//	
//	public byte[] show_private_key(){
//		return showPrivateKey();
//	}
}
