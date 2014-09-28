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
//	mac->GenerateKeyPairs();
//	mac->GenerateEphemeralKeys();
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

//	jbyteArray returns = env->NewByteArray(ephemKey.length());
//			env->SetByteArrayRegion(returns, 0, ephemKey.length(),
//					(jbyte*) ephemKey.c_str());
//	return returns;
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

//	jbyteArray returns = env->NewByteArray(publicKey.length());
//			env->SetByteArrayRegion(returns, 0, publicKey.length(),
//					(jbyte*) publicKey.c_str());
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
	jsize length_ek = env->GetStringLength(ephemeralkey_byte);
			const char *inCStr_ek = env->GetStringUTFChars(ephemeralkey_byte, NULL);
			string s_ek(inCStr_ek, length_ek);
			env->ReleaseStringUTFChars(ephemeralkey_byte, inCStr_ek);  // release resources
			jsize length_pk = env->GetStringLength(publickey_byte);
			const char *inCStr_pk = env->GetStringUTFChars(publickey_byte, NULL);
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

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_prepareEncryptionCPP
(JNIEnv *env, jobject thisObj, jboolean hmm, jboolean jinit) {
	bool init = jinit;
	if(init == true){
		mac->EncryptCertKey();
	}else{
		mac_B->EncryptCertKey();
	}
};

JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_getEncryptCertAndRCPP(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	bool init = jinit;
	string encryption;
	if(init == true){
		encryption = mac->GetCipher();
	}else{
		encryption = mac_B->GetCipher();
	}
//	jbyteArray returns = env->NewByteArray(encryption.length());
//	env->SetByteArrayRegion(returns, 0, encryption.length(),
//				(jbyte*) encryption.c_str());

	return env->NewStringUTF(encryption.c_str());

}
;

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setEncryptionFromPartyCPP
(JNIEnv *env, jobject thisObj, jboolean jinit, jbyteArray encryption_byte) {


};

JNIEXPORT jboolean JNICALL Java_com_example_androidake_MutualAuthenticateChip_verifCertificateCPP(
		JNIEnv *env, jobject thisObj, jboolean jinit) {
	jboolean verif = true;

	return verif;
}
;

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_computeSessionKeyCPP
(JNIEnv *env, jobject thisObj, jboolean jinit) {

};

JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getSessionKeyCPP(
		JNIEnv *env, jobject thisObj, jboolean jinit) {

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

//	jbyteArray returns = env->NewByteArray(pubKey.length());
//			env->SetByteArrayRegion(returns, 0, pubKey.length(),
//					(jbyte*) pubKey.c_str());
	return env->NewStringUTF(pubKey.c_str());
}
;


JNIEXPORT jboolean JNICALL Java_com_example_androidake_MutualAuthenticateChip_decodeEncryption
  (JNIEnv * env, jobject thisObj, jboolean jinit, jstring cipher){
	bool init = jinit;
	bool result;

	jsize length = env->GetStringLength(cipher);
				const char *inCStr_ek = env->GetStringUTFChars(cipher, NULL);
				string s(inCStr_ek, length);
	if(init == true){
		result = mac->DecryptCertKey(s);
	}else{
		result = mac_B->DecryptCertKey(s);
	}
	return result;
};


//JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_decodeEncryption
//  (JNIEnv * env, jobject thisObj, jboolean jinit, jstring cipher){
//	bool init = jinit;
//	bool result;
//
//	jsize length = env->GetStringLength(cipher);
//				const char *inCStr_ek = env->GetStringUTFChars(cipher, 0);
//				string s(inCStr_ek, length);
//				env->ReleaseStringUTFChars(cipher,inCStr_ek);
//				jbyteArray returns = env->NewByteArray(s.length());
//					env->SetByteArrayRegion(returns, 0, s.length(),
//							(jbyte*) s.c_str());
////					env->ReleaseByteArrayElements(result, returns, 0);
//					return returns;
//};

JNIEXPORT jstring JNICALL Java_com_example_androidake_MutualAuthenticateChip_getSomeString
  (JNIEnv * env, jobject thisObj, jboolean jinit){
	string dupa = "dupapapappa";
	return env->NewStringUTF(dupa.c_str());
};

