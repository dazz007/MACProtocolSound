package com.example.androidake;

public class MutualAuthenticateChip {
	
	public boolean initiated = false;
	static {
		System.loadLibrary("nativecryptowrapper");
	}
	
	public native void prepareMACCPP();

	public native byte[] getEphemeralKeyCPP();
	
	public native byte[] showKeyPair();
	
	public native byte[] showPrivateKey();
	
	public native void setEphemeralKeyFromPartyCPP(String ephemeralKey);
	
	public native void prepareEncryptionCPP(boolean initiated);
	
	public native byte[] getEncryptCertAndRCPP();
	
	public native void setEncryptionFromPartyCPP( byte [] encryption );
	
	public native boolean verifCertificateCPP();
	
	public native void computeSessionKeyCPP();
	
	public native byte[] getSessionKeyCPP();
	
	public native void setInitializatorCPP(boolean init);
	
	public void set_initializator(boolean init){
		this.initiated = init;
		setInitializatorCPP(init);
	}
	
	public void prepare_keys_and_cert(){
		prepareMACCPP();
	}
	
	public byte[] return_ephemeralkey(){
		return getEphemeralKeyCPP();
	}
	
	public void set_ephemeralkey_from_party(String ephemeralKey){
		setEphemeralKeyFromPartyCPP(ephemeralKey);
	}
	
	public byte[] get_encrypt_cert_and_R(){
		return getEncryptCertAndRCPP();
	}
	
	public void prepare_encryption(boolean initiated){
		prepareEncryptionCPP(this.initiated);
	}
	
	
	public void set_encryption_from_party( byte [] encryption ){
		setEncryptionFromPartyCPP(encryption);
	}
	
	public boolean verif_certificate(){
		return verifCertificateCPP();
	}
	
	public void compute_session_key(){
		computeSessionKeyCPP();
	}
	
	public void get_session_key(){
		byte[] sk = getSessionKeyCPP();
	}
	
	public byte[] show_key_pair(){
//		byte[] str = showKeyPair();
		
		
		return showKeyPair();
	}
	
	public byte[] show_private_key(){
		return showPrivateKey();
	}
}
