/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_androidake_MutualAuthenticateChip */

#ifndef _Included_com_example_androidake_MutualAuthenticateChip
#define _Included_com_example_androidake_MutualAuthenticateChip
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    prepareMACCPP
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_prepareMACCPP
  (JNIEnv *, jobject);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    getEphemeralKeyCPP
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getEphemeralKeyCPP
  (JNIEnv *, jobject);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    showKeyPair
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_showKeyPair
  (JNIEnv *, jobject);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    showKeyPair
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_showPrivateKey
  (JNIEnv *, jobject);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    setEphemeralKeyFromPartyCPP
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setEphemeralKeyFromPartyCPP
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    prepareEncryptionCPP
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_prepareEncryptionCPP
  (JNIEnv *, jobject, jboolean);


JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setInitializatorCPP
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    getEncryptCertAndRCPP
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getEncryptCertAndRCPP
  (JNIEnv *, jobject);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    setEncryptionFromPartyCPP
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_setEncryptionFromPartyCPP
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    verifCertificateCPP
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_example_androidake_MutualAuthenticateChip_verifCertificateCPP
  (JNIEnv *, jobject);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    computeSessionKeyCPP
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_androidake_MutualAuthenticateChip_computeSessionKeyCPP
  (JNIEnv *, jobject);

/*
 * Class:     com_example_androidake_MutualAuthenticateChip
 * Method:    getSessionKeyCPP
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_example_androidake_MutualAuthenticateChip_getSessionKeyCPP
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
