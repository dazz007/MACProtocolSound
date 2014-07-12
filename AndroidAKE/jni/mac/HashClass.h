#pragma once
#include <iostream>
#include <string>
#include "cryptopp/md5.h"
#include "cryptopp/sha.h"
#include "cryptopp/integer.h"
using namespace std;
using CryptoPP::Integer;


////////////////////////////////////////////////////////////
class HashClass{
public:
    static byte * getSHA1(const byte * input, int size);
    static Integer getSHA1Integer(string m, Integer r);
    static void printStringAsHex(byte *m, int length);
    static int size;
};
////////////////////////////////////////////////////////////

