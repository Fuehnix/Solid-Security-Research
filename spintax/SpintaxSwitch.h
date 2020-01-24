#pragma once
#include <string>
#include <vector>
class SpintaxSwitch
{
private:
	//TO DO:
	// Nesting...

	string switch_type;
	int minVal;
	int maxVal;
	vector<string> strings;
public:
	string getSwitchType();
	int getMinVal();
	int getMaxVal();
	vector<string> getStringOptions();
	void setSwitchType(string type);
	void setMinVal(int value);
	void setMaxVal(int value);
	int spinIntValue();
	string spinStringValue();
};

