#pragma once
#include <string>
#include <iostream>
#include <strstream>
#include <sstream>
#include "cryptopp/secblock.h"
using CryptoPP::SecByteBlock;

#include "cryptopp/integer.h"
using CryptoPP::Integer;

class Converter
{
public:
	static std::string SecByteBlockToString(SecByteBlock data);
	static std::string ByteToString(byte * data, int length);
	static void FromStringToSecByteblock(std::string data, SecByteBlock* sbb_data, int size);
	//static void TestIntegerAndSecByteBlock(Integer, SecByteBlock *);
	static Integer decodeSecByteBlock(SecByteBlock key);

	static SecByteBlock * createSecByteBlock(Integer key);
	static SecByteBlock encodeSecByteBlock(Integer key);
	static std::string IntegerToString(Integer a);
	static void test();
};

