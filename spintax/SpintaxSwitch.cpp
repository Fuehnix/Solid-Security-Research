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
	int tempMin = INT_MIN;
	string tempNumberString = "";
	bool containsDash = false;
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
			switch_type = strType;
			openBraces = true;
			substring.push_back(*i);
			continue;
		}
		if (*i == '}') {
			openBraces = false;
			substring.push_back(*i);
			continue;
		}
		//Possible examples
		//300-1400-         X
		//300-1400a         X
		//300-A unit cost   X
		//300--1400         X
		//All of these are not intType
		if (switch_type == noType)
			if(isdigit(*i)) {
				substring.push_back(*i);
				tempNumberString.push_back(*i);
			}
			else{
				//if we have a non number character, or, if we have seen a dash and then we see another dash, we know that this is not a intType
				if (*i != '-' || (containsDash && *i == '-')) {
					switch_type = strType;
					//tempMin != INT_MIN signals that we have set the value of a number
					//now that it is strType, we must add this text back in.
					if (tempMin != INT_MIN) {
						substring.append(tempNumberString);
					}
					substring.push_back(*i);
					continue;
				}
				else if (!containsDash && *i == '-'){
					containsDash = true;
					tempMin = stoi(substring);
					tempNumberString.push_back('-');
					substring = "";
					//skip over '-' by not doing a pushback
				}
			}
		if (switch_type == strType) {
			substring.push_back(*i);
		}
		if (i == in.end()) {
			substring.push_back(*i);
			if (switch_type == noType) {
				switch_type == intType;
				minVal = tempMin;
				maxVal = stoi(substring);
			}
			if (switch_type == strType) {
				strings.push_back(substring);
			}
		}
	}
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

//void SpintaxSwitch::setMinVal(int value) {

//}

//void SpintaxSwitch::setMaxVal(int value) {

//}

//returns the input string with the int value in the place of the spintax
int SpintaxSwitch::spinIntValue() {

}

//returns the input string with the string value in the place of the spintax
string SpintaxSwitch::spinStringValue() {

}

bool SpintaxSwitch::getNested() {
	return isNested;
}
}