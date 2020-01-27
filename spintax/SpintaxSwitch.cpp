#include "SpintaxSwitch.h"

SpintaxSwitch::SpintaxSwitch(string in) {
	input = in;
	if (!(in.front() == '{' && in.back() == '}')) {
		cout << "Error: string passed is not a bounded switch statement" << endl;
	}
	//remove brackets from the string, leaving only the switch options.
	in.erase(0, 1);
	in.pop_back();
	string modInput;
	bool openBraces = false;
	string substring = "";
	//if nested is true, we know that this level must be a string switch with nesting, not integer
	for(string::const_iterator i = in.begin(); i <= in.end(); ++i) {
		if (!openBraces && *i == '|') {
			strings.push_back(substring);
			substring = "";
			continue;
		}
		//if escape character, iterate forward, push it no matter what it is, then continue to the character after that
		if (*i == '/') {
			substring.push_back(*i);
			++i;
			substring.push_back(*i);
			continue;
		}
		if (*i == '{') {
			isNested = true;
			openBraces = true;
			substring.push_back(*i);
			continue;
		}
		if (*i == '}') {
			openBraces = false;
			substring.push_back(*i);
			continue;
		}
		if (switch_type == noType && !isdigit(*i) && *i != '-') {
			switch_type = strType;
			substring.push_back(*i);
			continue;
		}
		if (switch_type == strType) {
			substring.push_back(*i);
		}
	}
	if (switch_type == noType) {
		
	}
	stringstream stream(in);
	
}

int SpintaxSwitch::getSwitchType(){
	return switch_type;
}

int SpintaxSwitch::getMinVal() {
	if (switch_type == intType) {
		return minVal;
	} else {
		cout << "Error: Invalid switch type" << endl;
	}
}

int SpintaxSwitch::getMaxVal() {
	if (switch_type == intType) {
		return maxVal;
	}
	else {
		cout << "Error: Invalid switch type" << endl;
	}
}

vector<string> SpintaxSwitch::getStringOptions() {
	return strings;
}

void SpintaxSwitch::setMinVal(int value) {

}

void SpintaxSwitch::setMaxVal(int value) {

}

//returns the input string with the int value in the place of the spintax
int SpintaxSwitch::spinIntValue() {

}

//returns the input string with the string value in the place of the spintax
string SpintaxSwitch::spinStringValue() {

}

bool SpintaxSwitch::getNested() {
	return isNested;
}