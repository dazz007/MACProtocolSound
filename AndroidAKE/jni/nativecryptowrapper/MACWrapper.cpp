#include "include/MACWrapper.h"
#include "mac/MutualAuthenticationChip.h"

MutualAuthenticationChip *mac;
MutualAuthenticationChip *mac_B;

//static MutualAuthenticationChip mac;
JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_prepareMACCPP
(JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
	if(init == true) {
		mac = new MutualAuthenticationChip(init);
	} else {
		mac_B = new MutualAuthenticationChip(init);
	}
};

JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_getEphemeralKeyCPP(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
	string ephemKey;
	if (init == true) {
		ephemKey = mac->GetEphemeralPublicKey();
	} else {
		ephemKey = mac_B->GetEphemeralPublicKey();
	}
	return env->NewStringUTF(ephemKey.c_str());

};

JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_showKeyPair(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
	string publicKey;
	if (init) {
		 publicKey = mac->ShowPublicKey();
	} else {
		 publicKey = mac_B->ShowPublicKey();
	}
	return env->NewStringUTF(publicKey.c_str());
}
;

JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_showPrivateKey(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	string privatekey = mac->ShowPrivateKey();

	jbyteArray returns = env->NewByteArray(privatekey.length());
	env->SetByteArrayRegion(returns, 0, privatekey.length(),
			(jbyte*) privatekey.c_str());
	return returns;
}
;

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setEphemeralAndPublicKeyFromPartyCPP
(JNIEnv *env, jobject thisObj, jboolean jinit, jstring ephemeralkey_byte, jstring publickey_byte) {
	bool init = jinit;
	jsize length_ek = env->GetStringUTFLength(ephemeralkey_byte);
	const char *inCStr_ek = env->GetStringUTFChars(ephemeralkey_byte, 0);
	string s_ek(inCStr_ek, length_ek);
	env->ReleaseStringUTFChars(ephemeralkey_byte, inCStr_ek);  // release resources
	jsize length_pk = env->GetStringUTFLength(publickey_byte);
	const char *inCStr_pk = env->GetStringUTFChars(publickey_byte, 0);
	string s_pk(inCStr_pk, length_pk);
	env->ReleaseStringUTFChars(publickey_byte, inCStr_pk);  // release resources

	if(init == true) {
		mac->SetEphemeralPublicKeyAnotherParty(s_ek,s_pk);
	} else {
		mac_B->SetEphemeralPublicKeyAnotherParty(s_ek,s_pk);
	}


}

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setEphemeralKeyFromPartyCPP
(JNIEnv *env, jobject thisObj, jstring ephemeralkey_byte, jboolean jinit) {

};

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setInitializatorCPP
(JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
	//mac.SetInitializator(init);
};

JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_prepareEncryptionCPP
(JNIEnv *env, jobject thisObj, jboolean hmm, jboolean jinit) {
	bool init = jinit;
	string encryption;
	if(init == true){
		encryption = mac->EncryptCertKey();
	}else{
		encryption = mac_B->EncryptCertKey();
	}
	jbyteArray returns = env->NewByteArray(encryption.size());
	env->SetByteArrayRegion(returns, 0, encryption.length(), (jbyte*) encryption.c_str());

	return returns;
};


JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getEncryptCertAndRCPP(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
		string encryption;
		if(init == true){
			encryption = mac->GetCipher2();
		}else{
			encryption = mac_B->GetCipher2();
		}
	jbyteArray returns = env->NewByteArray(encryption.length());
	env->SetByteArrayRegion(returns, 0, encryption.length(), (jbyte*) encryption.c_str());

	return returns;
};


JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_getSessionKeyCPP(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
	string sessionKey;
	if (init == true) {
		sessionKey = mac->ShowSessionKey();
	} else {
			sessionKey = mac_B->ShowSessionKey();
	}
	return env->NewStringUTF(sessionKey.c_str());
}
;

JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_getPublicKeyAnotherPartyCPP(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
	string pubKey;
	if (init == true) {
		 pubKey = mac->ShowOtherPartyPublicKey();
	} else {
		 pubKey = mac_B->ShowOtherPartyPublicKey();
	}
	return env->NewStringUTF(pubKey.c_str());
}
;


JNIEXPORT jboolean JNICALL Java_com_example_androidake_MutualAuthenticateChip_decodeEncryption
  (JNIEnv * env, jobject thisObj, jboolean jinit, jbyteArray cipher){
	bool init = jinit;
	bool result;

	jbyte* data = env->GetByteArrayElements(cipher, 0);
	jsize length = env->GetArrayLength(cipher);

	byte * data_cipher = (byte *) data;

	int size = length;
	if(init == true){
		result = mac->DecryptCertKeyByte(data_cipher, size);
		}else{
			result = mac_B->DecryptCertKeyByte(data_cipher, size);
		}

	env->ReleaseByteArrayElements(cipher, data, JNI_ABORT);
	return result;
};



JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_getSomeString
  (JNIEnv * env, jobject thisObj, jboolean jinit){
	string dupa = "dupapapappa";
	return env->NewStringUTF(dupa.c_str());
};


JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_getPublicAnotherParty22CPP
  (JNIEnv * env, jobject thisObj, jboolean jinit){
	bool init = jinit;

	string publicKey;
		if (init) {
			 publicKey = mac->ShowPublicKeyAnotherParty2();
		} else {
			 publicKey = mac_B->ShowPublicKeyAnotherParty2();
		}
		return env->NewStringUTF(publicKey.c_str());
};


JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_convertStringToByteArray(JNIEnv* env, jobject clazz, jstring str)
{
	const char* nativeString = env->GetStringUTFChars(str, 0);
	size_t length = strlen(nativeString);

	jbyteArray array = env->NewByteArray(length);
	env->SetByteArrayRegion(array, 0, length, (const jbyte*)nativeString);

	env->ReleaseStringUTFChars(str, nativeString);

	return array;
}


/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    convertByteArrayToString
 * Signature: ([B)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_convertByteArrayToString(JNIEnv* env, jobject clazz, jbyteArray text_byte)
{
	jsize length = env->GetArrayLength(text_byte);
	jbyte* buffer = new jbyte[length+1];

	env->GetByteArrayRegion(text_byte, 0, length, buffer);
	buffer[length] = '\0';

	jstring result = env->NewStringUTF((const char*)buffer);
	delete[] buffer;

	return result;
}





