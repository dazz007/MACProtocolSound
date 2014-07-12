#include "MyDebugger.h"

MyDebugger::MyDebugger()
{
    m_debug = false;
}

void MyDebugger::setDebug(bool debug)
{
    m_debug = debug;
}

void MyDebugger::debug(string part, const char* message)
{
    if(m_debug)
    {
        cout << message << endl;
    }
}
