// redaction.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
#include <pugixml.hpp>
//#include "pugixml.cpp"
// reading a text file
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <sstream> 
using namespace std;

vector<string> parse(string input, char c);
//expected runtime format is
//   ./redac input.xml output.txt --clearance SCI,TS,S,CNF
int main(int argc, char *argv[]){
	if(argc == 1){
		cout << "Error: must include input xml file" << endl;
	}
	string inputFName = argv[2];
	string outputFName = argv[3];
	ifstream inputXML(inputFName);
	if (inputXML.is_open()) {
		cout << "Input file loaded successfully" << endl;
		inputXML.close();
	}
	ofstream output(outputFName);
	bool clearance = false;
	string clearances = "";
	//get vector of the clearances that this person has access to.
	for(int i = 0; i < argc; i++){
		string temp = argv[i];
		if (clearance) {
			clearances = temp;
		}
		if (temp.find("--clearance") != string::npos) {
			clearance = true;
		}
	}
	
	std::vector<string> clearancesV = parse(clearances,',');
	int wordL = 5;
	int wordsL = 11;
	int sentenceL = 90;
	int sentencesL = 181;
	int paragraphL = 363;
	
	string line;
	//if (inputXML.is_open()){
	//	while (getline(inputXML, line)){
	//		cout << line << '\n';
	//	}
	//	inputXML.close();
	//} 
	//else 
	//	cout << "Unable to open file";

	pugi::xml_document doc;

	char* fchar = new char[inputFName.size() + 1];
	inputFName.copy(fchar, inputFName.size() + 1);
	fchar[inputFName.size()] = '\0';
	pugi::xml_parse_result result = doc.load_file(fchar);
	if (result) {
		cout << "Successfully loaded document into XML parser" << endl;
	} 
	else {
		cout << "Error: failed to parse XML document" << endl;
	}

	//create a node for the text of the document
	pugi::xml_node text = doc.child("document").child("text");

	//parse through redactions
	for (pugi::xml_node redact = text.child("redact"); redact; redact = redact.next_sibling("redact"))
	{
		int replaceLength = 0;
		bool shouldRedact = true;
		for (pugi::xml_attribute attr = redact.first_attribute(); attr; attr = attr.next_attribute()){
			std::string name = attr.name();
			std::string value = attr.value();
			//assign length 
			if (name == "type") {
				if (value == "Word") {
					replaceLength = wordL;
				}
				else if (value == "Words") {
					replaceLength = wordsL;
				}
				else if (value == "Sentence") {
					replaceLength = sentenceL;
				}
				else if (value == "Sentences") {
					replaceLength = sentencesL;
				}
				else if (value == "Paragraph") {
					replaceLength = paragraphL;
				}
			}
			if (name == "level") {
				//int i = std::find(std::begin(levels), std::end(levels), attr.name());
				if (std::end(clearancesV) != std::find(std::begin(clearancesV), std::end(clearancesV), value)) {
					shouldRedact = false;
				}
			}
		}
		if (!shouldRedact) {
			//std::c/*out << "redact:";
			//std::cout << redact.text().get();
			//std::cout << "you can see me" << std::endl;*/
			//std::cout << " " << attr.name() << "=" << attr.value() << std::endl;
		}
		else if (shouldRedact) {
			//create string of Full Block characters of length replaceLength
			//must be converted to char * array because pugixml will not accept std::string
			string redacted = string(replaceLength, 'X');
			char *cstr = new char[replaceLength + 1];
			redacted.copy(cstr, redacted.size() + 1);
			cstr[redacted.size()] = '\0';
			redact.text().set(cstr);
		}
	}
	//create a text file from our redacted pugixml object
	for (pugi::xml_node_iterator it = text.begin(); it != text.end(); ++it)
	{
		//std::cout << "text:";
		//std::cout << it->text().get();
		output << it->text().get();
		//std::cout << std::endl;
	}
	output.close();
	cout << "Document is finished being redacted" << endl;
	//std::cout << "Document:\n";
	//doc.save(std::cout);
	//std::cout << std::endl;
}

vector<string> parse(string input, char c) {
	stringstream stream(input);
	vector<string> parsedVector;
	//cout << "this is input" << input << endl;
	while (stream.good()) {
		string substring;
		getline(stream, substring, ',');
		//cout << substring << endl;
		parsedVector.push_back(substring);
	}
	return parsedVector;
}
// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
