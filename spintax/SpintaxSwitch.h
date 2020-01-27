#pragma once
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
using namespace std;

class SpintaxSwitch
{
private:
	//TO DO:
	// Nesting...
	// Global ...
	// escape characters
	const int noType = -1;
	const int intType = 0;
	const int strType = 1;
	int switch_type = noType;
	int minVal;
	int maxVal;
	vector<string> strings;
	string input;
	bool isNested = false;
public:
	SpintaxSwitch(string in);
	int getSwitchType();
	int getMinVal();
	int getMaxVal();
	vector<string> getStringOptions();
	void setMinVal(int value);
	void setMaxVal(int value);
	int spinIntValue();
	string spinStringValue();
	bool getNested();
};

