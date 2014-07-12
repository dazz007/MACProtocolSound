#include "include/MACWrapper.h"
#include "mac/MutualAuthenticationChip.h"

static MutualAuthenticationChip mac(true);

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_prepareMACCPP
  (JNIEnv *env, jobject thisObj){
	mac.GenerateKeyPairs();
	mac.GenerateEphemeralKeys();
};


JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getEphemeralKeyCPP
  (JNIEnv *env, jobject thisObj){

	string ephemKey = mac.GetEphemeralPublicKey();

	jbyteArray returns = env->NewByteArray(ephemKey.length());
	env->SetByteArrayRegion(returns, 0, ephemKey.length(), (jbyte*)ephemKey.c_str());
	return returns;
};

JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_showKeyPair(JNIEnv *env, jobject thisObj){

		string publicKey = mac.ShowPublicKey();

		jbyteArray returns = env->NewByteArray(publicKey.length());
		env->SetByteArrayRegion(returns, 0, publicKey.length(), (jbyte*)publicKey.c_str());
		return returns;
};

JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_showPrivateKey(JNIEnv *env, jobject thisObj){
		string privatekey = mac.ShowPrivateKey();

		jbyteArray returns = env->NewByteArray(privatekey.length());
		env->SetByteArrayRegion(returns, 0, privatekey.length(), (jbyte*)privatekey.c_str());
		return returns;
};

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setEphemeralKeyFromPartyCPP
  (JNIEnv *env, jobject thisObj, jstring ephemeralkey_byte){
//	jsize length = env->GetStringLength(ephemeralkey_byte);
//
//	const char *inCStr = env->GetStringUTFChars(ephemeralkey_byte, NULL);
//	string s(inCStr, length);
//	mac.SetEphemeralPublicKeyAnotherParty(s);
};

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setInitializatorCPP
	(JNIEnv *env, jobject thisObj, jboolean jinit){
	bool init = jinit;
	mac.SetInitializator(init);
};

JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_prepareEncryptionCPP
  (JNIEnv *env, jobject thisObj, jboolean){


};


JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getEncryptCertAndRCPP
  (JNIEnv *env, jobject thisObj){
	jbyteArray encryption_byte;

	return encryption_byte;

};


JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setEncryptionFromPartyCPP
  (JNIEnv *env, jobject thisObj, jbyteArray encryption_byte){


};


JNIEXPORT jboolean JNICALL Java_com_example_androidake_MutualAuthenticateChip_verifCertificateCPP
  (JNIEnv *env, jobject thisObj){
	jboolean verif = true;

	return verif;
};


JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_computeSessionKeyCPP
  (JNIEnv *env, jobject thisObj){


};


JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getSessionKeyCPP
  (JNIEnv *env, jobject thisObj){


};
