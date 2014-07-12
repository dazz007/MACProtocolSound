#pragma once
#include <iostream>
using namespace std;
class MyDebugger
{
private:
        bool m_debug;

    public:
        MyDebugger();
        void setDebug(bool debug);
        void debug(string part, const char* message);
};

