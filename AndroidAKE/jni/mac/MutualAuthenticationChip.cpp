#include "MutualAuthenticationChip.h"


void MutualAuthenticationChip::GenerateKeyPairs(AutoSeededRandomPool &rnd){
	privateKey = new SecByteBlock(dh.PrivateKeyLength()); //xA - private key
	publicKey = new SecByteBlock(dh.PublicKeyLength());   //yA = g^xA - public key
	kg->GenerateStaticKeyPair(rnd, *privateKey, *publicKey);
	cout<<"Part: "<<part<<" Zostaly wygenerowane klucze"<<endl;
}

void MutualAuthenticationChip::GenerateEphemeralKeys(AutoSeededRandomPool &rnd){
	ephemeralPublicKey = new SecByteBlock(dh2->EphemeralPublicKeyLength()); //hA = H(a)
	ephemeralPrivateKey = new SecByteBlock(dh2->EphemeralPrivateKeyLength()); //cA = g^hA

    kg->GenerateEphemeralKeyPair2(rnd, ephemeralPrivateKey, ephemeralPublicKey);

	//TestOfPower();

	cout<<"Part: "<<part<<" Zostaly wygenerowane klucze efemeryczne"<<endl;
}

void MutualAuthenticationChip::Generate2(SecByteBlock * pubKeyB, SecByteBlock * privKeyB){
	rnd.Reseed();
	kg->GenerateEphemeralKeyPair2(rnd, privKeyB, pubKeyB);
}

std::string MutualAuthenticationChip::GetEphemeralPublicKey(){
	return Converter::SecByteBlockToString(*ephemeralPublicKey);
}


int MutualAuthenticationChip::GetKeySize(){
	return keySize;
}

std::string MutualAuthenticationChip::ShowPublicKey(){
	std::string s = Converter::SecByteBlockToString(*publicKey);
	return s;
}

std::string MutualAuthenticationChip::ShowPublicKeyAnotherParty2(){
	std::string s = Converter::SecByteBlockToString(*publicKeyAnotherParty);
	return s;
}

std::string MutualAuthenticationChip::ShowPrivateKey(){
	std::string s = Converter::SecByteBlockToString(*privateKey);
	return s;
}



void MutualAuthenticationChip::GetEphemeralPublicKey2(byte * epubK, size_t &size){
}


void MutualAuthenticationChip::SetEphemeralPublicKeyAnotherParty(std::string str_ephemeralPublicKeyAnotherParty,
																 std::string str_publicKeyAnotherParty){
	CryptoPP::Integer K;
	CryptoPP::Integer int_Kx_prim;
	CryptoPP::Integer cb_to_xa;

	byte * Kx_prim = new byte[AES::DEFAULT_KEYLENGTH]; // KA_prim = H(K,3)
	cout<<"Part: "<<part<<" otrzymal klucz efemeryczny od drugiej strony"<<endl;

	this->K_byte = new SecByteBlock(AES::DEFAULT_KEYLENGTH); //K = cB^hA

	this->rA = new byte[HashClass::size]; //rA = H(cB^xA, KA_prim)

	//set public key another party
	SetKeysAnotherParty(str_ephemeralPublicKeyAnotherParty,str_publicKeyAnotherParty);

	K = ComputeK();//K = cB^hA

	//cout<<"K partu: "<<part<<" "<<K<<endl;
	K.Encode(*this->K_byte, AES::DEFAULT_KEYLENGTH);


	if(is_initializator){
		Kx_prim = kg->GenerateKeyFromHashedKey(K, 3, AES::DEFAULT_KEYLENGTH); // Kb_prim = H(K,3)
	}else{
		Kx_prim = kg->GenerateKeyFromHashedKey(K, 4, AES::DEFAULT_KEYLENGTH); // Kb_prim = H(K,3)
	}
	cb_to_xa = ComputeEphemeralKeyAnotherValueToPrivateKey(); // cB^xA
	int_Kx_prim.Decode(Kx_prim, AES::DEFAULT_KEYLENGTH);

	rA = kg->GenerateKeyFromHashedKey(cb_to_xa, int_Kx_prim, AES::DEFAULT_KEYLENGTH ); //rA = H(cB^xA, KA_prim)

}



std::string MutualAuthenticationChip::EncryptCertKey(){
	CryptoPP::Integer K = ComputeK();
		std::string cert = "testowowowona pewnoe jkhsdajgdjhgjbcmxzgigsajdghsma bjjdgsagdj";
		const char* cert_c = cert.c_str();
		cert.size();
		byte * cert_b = (byte*)cert_c;
		byte * Kx = new byte[AES::DEFAULT_KEYLENGTH];
		//cout<<"PArt: "<<part<<" K przed szyfracja: "<<K<<endl;
	  	if(is_initializator){
			Kx = kg->GenerateKeyFromHashedKey(K, 1, AES::DEFAULT_KEYLENGTH); //Ka = H(K,1)
		}else{
			Kx = kg->GenerateKeyFromHashedKey(K, 2, AES::DEFAULT_KEYLENGTH); //Kb = H(K,2)
		}
		Integer Kx_test(Kx, AES::DEFAULT_KEYLENGTH);
		//cout<<"PArt: "<<part<<" Klucz do szyfracji :"<<Kx_test<<endl;
		this->cipher = edc.EncryptCertAndRa(cert_b, (int)cert.size(),
	                                        rA, HashClass::size,
	                                        Kx, AES::DEFAULT_KEYLENGTH);

		cout<<"Part: "<<part<<" zaszyfrowal certyfikat i rA "<< cipher << endl;
	    return cipher;
}


bool MutualAuthenticationChip::CompareCipher(std::string cipher_2){
	cipher_two = cipher_2;
	if(cipher == cipher_2){
		return true;
	}else{
		return false;
	}
}

bool MutualAuthenticationChip::CompareCipherUTF8(byte * data, int length){
	std::string cipher_2( reinterpret_cast<char const*>(data), length);
	if(cipher == cipher_2){
		return true;
	}else{
		return false;
	}
}


std::string MutualAuthenticationChip::GetCipher(){
	if(cipher.compare("")!=0){
//		string s("dziwne rzeczy");
		return cipher;
	}else{
		std::string s("japierdole");
		return s;
	}
}

std::string MutualAuthenticationChip::GetCipher2(){
	return cipher_two;
}

bool MutualAuthenticationChip::DecryptCertKeyByte(byte * data, int length){
	std::string cipher_2( reinterpret_cast<char const*>(data), length);
	CryptoPP::Integer K = ComputeK();
		std::string decrypted_cert;
		byte * decrypted_ra = new byte[HashClass::size];
		byte * rA_prim = new byte[HashClass::size];
		byte * Kx = new byte[AES::DEFAULT_KEYLENGTH];
		int decrypted_ra_size;

		//cout<<"PArt: "<<part<<" K do deszyfracj: "<<K<<endl;

		if(is_initializator){
			Kx = kg->GenerateKeyFromHashedKey(K, 2, AES::DEFAULT_KEYLENGTH); //Kb = H(K,2)

		}else{
			Kx = kg->GenerateKeyFromHashedKey(K, 1, AES::DEFAULT_KEYLENGTH); //Ka = H(K,1)
		}

		Integer Kx_test(Kx, AES::DEFAULT_KEYLENGTH);
		cout<<"PArt: "<<part<<"Klucz do deszyfracji :"<<Kx_test<<endl;
		edc.DecryptCertAndRa(cipher_2, Kx, AES::DEFAULT_KEYLENGTH,
								&decrypted_cert, decrypted_ra, &decrypted_ra_size);

		int n = CompareRa(decrypted_ra);
		if(n == 0){
			cout<<"Po stronie: "<<part<<" Ra takie samo po deszyfracji jak dla strony przeciwnej"<<endl;
			ComputeSessionKey();
			return true;
		}else{
			cout<<"Weryfikacja nie powiodla sie. Protocol failure."<<endl;
			return false;
		}
}

bool MutualAuthenticationChip::DecryptCertKey(std::string cipher){
	CryptoPP::Integer K = ComputeK();
	std::string decrypted_cert;
	byte * decrypted_ra = new byte[HashClass::size];
	byte * rA_prim = new byte[HashClass::size];
	byte * Kx = new byte[AES::DEFAULT_KEYLENGTH];
	int decrypted_ra_size;

	//cout<<"PArt: "<<part<<" K do deszyfracj: "<<K<<endl;

	if(is_initializator){
		Kx = kg->GenerateKeyFromHashedKey(K, 2, AES::DEFAULT_KEYLENGTH); //Kb = H(K,2)

	}else{
		Kx = kg->GenerateKeyFromHashedKey(K, 1, AES::DEFAULT_KEYLENGTH); //Ka = H(K,1)
	}

	Integer Kx_test(Kx, AES::DEFAULT_KEYLENGTH);
	cout<<"PArt: "<<part<<"Klucz do deszyfracji :"<<Kx_test<<endl;
	edc.DecryptCertAndRa(cipher, Kx, AES::DEFAULT_KEYLENGTH,
							&decrypted_cert, decrypted_ra, &decrypted_ra_size);

	int n = CompareRa(decrypted_ra);
	if(n == 0){
		cout<<"Po stronie: "<<part<<" Ra takie samo po deszyfracji jak dla strony przeciwnej"<<endl;
		ComputeSessionKey();
		return true;
	}else{
		cout<<"Weryfikacja nie powiodla sie. Protocol failure."<<endl;
		return false;
	}

}


int MutualAuthenticationChip::CompareRa(byte * decrypted_ra){
	Integer yA_to_hB, int_Kx_prim, K;

	byte * Kx_prim = new byte[AES::DEFAULT_KEYLENGTH]; // KA_prim = H(K,3)
	byte * to_check = new byte[HashClass::size]; //rA

	yA_to_hB = ComputePublicKeyAnotherPartyToEphemeralKey();
	K = ComputeK();

	if(is_initializator){
		Kx_prim = kg->GenerateKeyFromHashedKey(K, 4, AES::DEFAULT_KEYLENGTH); // Kb_prim = H(K,4)
	}else{
		Kx_prim = kg->GenerateKeyFromHashedKey(K, 3, AES::DEFAULT_KEYLENGTH); // Ka_prim = H(K,3)
	}

	int_Kx_prim.Decode(Kx_prim, AES::DEFAULT_KEYLENGTH);

	to_check = kg->GenerateKeyFromHashedKey(yA_to_hB, int_Kx_prim, AES::DEFAULT_KEYLENGTH ); //rA = H(cB^xA, KA_prim)
	return memcmp( to_check, decrypted_ra, HashClass::size);
}

SecByteBlock MutualAuthenticationChip::GetEphemeralPublicKey2(){
	return *ephemeralPublicKey;
}

SecByteBlock MutualAuthenticationChip::GetPublicKey(){
	return *publicKey;
}

Integer MutualAuthenticationChip::ComputeK(){
	CryptoPP::Integer Cb;

	CryptoPP::Integer hA;

	Cb.Decode(ephemeralPublicKeyAnotherParty->BytePtr(), ephemeralPublicKeyAnotherParty->SizeInBytes());
	hA.Decode(ephemeralPrivateKey->BytePtr(), ephemeralPrivateKey->SizeInBytes());

	return a_exp_b_mod_c(Cb, hA, this->p); //K = cB^hA
}

Integer MutualAuthenticationChip::ComputeEphemeralKeyAnotherValueToPrivateKey(){
	CryptoPP::Integer Cb;
	CryptoPP::Integer xA;
	xA.Decode(this->privateKey->BytePtr(), this->privateKey->SizeInBytes()); //xA - private key
	Cb.Decode(ephemeralPublicKeyAnotherParty->BytePtr(), ephemeralPublicKeyAnotherParty->SizeInBytes());
	return a_exp_b_mod_c(Cb, xA, this->p); // cB^xA
}

Integer MutualAuthenticationChip::ComputePublicKeyAnotherPartyToEphemeralKey(){
	Integer yA;
	Integer hB;
	yA.Decode(publicKeyAnotherParty->BytePtr(), publicKeyAnotherParty->SizeInBytes());
	hB.Decode(ephemeralPrivateKey->BytePtr(), ephemeralPrivateKey->SizeInBytes());
	return a_exp_b_mod_c(yA, hB, this->p);

}



void MutualAuthenticationChip::SetKeysAnotherParty(std::string str_ephemeralPublicKeyAnotherParty,
						 std::string str_publicKeyAnotherParty){
	publicKeyAnotherParty = new SecByteBlock(dh.PublicKeyLength());

	Converter::FromStringToSecByteblock(str_publicKeyAnotherParty, publicKeyAnotherParty, dh.PublicKeyLength());

	ephemeralPublicKeyAnotherParty = new SecByteBlock(dh2->EphemeralPublicKeyLength());
	Converter::FromStringToSecByteblock(str_ephemeralPublicKeyAnotherParty, ephemeralPublicKeyAnotherParty, dh2->EphemeralPublicKeyLength());
}


void MutualAuthenticationChip::TestOfPower(){
	Integer ephemeralPub;
	ephemeralPub.Decode(ephemeralPublicKey->BytePtr(), ephemeralPublicKey->SizeInBytes());
	Integer privKey(privateKey->BytePtr(), privateKey->SizeInBytes());

	Integer pubKey(publicKey->BytePtr(), publicKey->SizeInBytes());
	Integer ephemeralPriv(ephemeralPrivateKey->BytePtr(), ephemeralPrivateKey->SizeInBytes());

	cout<<" Pierwsza czesc -----------------------------------------"<<endl;
	cout<< a_exp_b_mod_c(ephemeralPub, privKey, this->p);
	cout<<" Druga czesc -----------------------------------------"<<endl;
	cout<< a_exp_b_mod_c(pubKey, ephemeralPriv, this->p);
}

void MutualAuthenticationChip::ComputeSessionKey(){
	Integer K = ComputeK();
	byte * session_key = new byte[HashClass::size];
	session_key = kg->GenerateKeyFromHashedKey(K, 5, AES::DEFAULT_KEYLENGTH); //K_session
	K_session_key = new SecByteBlock(HashClass::size);
	K_session_key->Assign(session_key,HashClass::size);
	Integer SK(*K_session_key,HashClass::size);
	cout<<"WYGENEROWANO KLUCZ SESYJNY DLA PARTU "<<part<<endl;
	cout<<SK<<endl;
}




std::string MutualAuthenticationChip::ShowOtherPartyPublicKey()
{
    Integer pubKey;
    if (is_initializator == false)
    {
         pubKey = CryptoPP::Integer("30222313103416484737728074612425214126734791862902310075404137175196490255087357276634530623209055864437547252587560242012951252123883845471428300996630382283841646354968561208085927173525510706082475459851860792820176457822967649557768154303922217472248717392402506600192733705517151755320886806822808293568");
    }
    else
    {
        pubKey = CryptoPP::Integer("14937252435456816444315144988606834394405620177779470871314613282765209100656000546289874717514307843372460147271927179132591019166403117763752044377923030284277665081681429636803343669361207907144378001193776859330592525966966787427742150822910770827385205862373202130377453205416037048248518669196478538474");
    }
    return Converter::SecByteBlockToString(Converter::encodeSecByteBlock(pubKey));
}
