package com.example.androidake;

public class MutualAuthenticateChip {
	
	public boolean initiated = false;
	static {
		System.loadLibrary("nativecryptowrapper");
	}
	/**
	 * Prepare cryptographic data
	 */
	public native void prepareMACCPP(boolean init);
	
	/**
	 * Get ephemeral key
	 * @param init - initiator
	 * 
	 */
	public native String getEphemeralKeyCPP(boolean init);
	
	/**
	 * Show key pair
	 * @param init - initiator
	 * @return string keys
	 */
	public native String showKeyPair(boolean init);
	
	/**
	 * Show key pair
	 * @param init - initiator
	 * 
	 */
	public native byte[] showPrivateKey(boolean init);
	
	/**
	 * Get public key from another party
	 * @param init - initiator
	 */
	public native String getPublicKeyAnotherPartyCPP(boolean init);
	
	/**
	 * Set ephemeral and public key from party
	 * @param init - initiator
	 * @param ephemeralKey - ephemeral key
	 * @param publicKey - public key
	 */
	public native void setEphemeralAndPublicKeyFromPartyCPP(boolean init, String ephemeralKey, String publicKey);
	
	/**
	 * Prepare encryption
	 * @param init - initiator
	 * @param initiated
	 * @return return byte
	 */
	public native byte[] prepareEncryptionCPP(boolean init, boolean initiated);
	
	/**
	 * Get encrypted certificate and challenge value r
	 * @param init - initiator
	 * @return encrypted certificate and challenge value 
	 */
	public native byte[] getEncryptCertAndRCPP(boolean init);
	
	/**
	 * Set encryption from other party
	 * @param init - initiator
	 * @param encryption - encryption
	 */
	public native void setEncryptionFromPartyCPP(boolean init, byte [] encryption );
	
	/**
	 * Get some string - test method
	 * @param init - initiator
	 * @return string
	 */
	public native String getSomeString(boolean init);
	
	/**
	 * Decode encryption
	 * @param init - initiator
	 * @return - ciphertext
	 */
	public native boolean decodeEncryption(boolean init, byte [] cipher);
	
	/**
	 * Convert string to byte array
	 * @param text - text
	 * @return byte array
	 */
	public native byte[] convertStringToByteArray(String text);
	
	/**
	 * Convert byte array to string
	 * @param text_byte - text in bytes
	 * @return string
	 */
	public native String convertByteArrayToString(byte [] text_byte);
	
	/**
	 * Get session key
	 * @param init - initiator
	 * @return string
	 */
	public native String getSessionKeyCPP(boolean init);
	
	/**
	 * Get public key another party
	 * @param init - initiator
	 * @return string
	 */
	public native String getPublicAnotherParty22CPP(boolean init);
	
	//-------------------------------
	
	
	
	
	public void set_initializator(boolean init){
		this.initiated = init;
		//setInitializatorCPP(init);
	}

}
