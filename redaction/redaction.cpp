// redaction.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
#include <pugixml.hpp>
//#include "pugixml.cpp"
// reading a text file
#include <iostream>
#include <fstream>
#include <string>
using namespace std;

//int argc, char **argv
int main(){
	int wordL = 5;
	int wordsL = 11;
	int sentenceL = 90;
	int sentencesL = 181;
	int paragraphL = 363;
	string levels[] = { "S", "CNF" };
	string line;
	ifstream myfile("example.xml");
	ofstream fout("output.xml");
	if (myfile.is_open()){
		while (getline(myfile, line)){
			cout << line << '\n';
		}
		myfile.close();
	} else 
		cout << "Unable to open file";

	pugi::xml_document doc;
	std::string namepanel;
	
	pugi::xml_parse_result result = doc.load_file("example.xml");
	if (result) {
		cout << "success" << endl;
	}
	else {
		cout << "fail" << endl;
	}
	//pugi::xml_node project = doc.child("document");

	//std::cout << "Project name: " << project.child("name").text().get() << std::endl;
	//std::cout << "Project version: " << project.child("version").text().as_double() << std::endl;
	//std::cout << "Project visibility: " << (project.child("public").text().as_bool(/* def= */ true) ? "public" : "private") << std::endl;
	//std::cout << "Project description: " << project.child("description").text().get() << std::endl;

	pugi::xml_node text = doc.child("document").child("text");
	//pugi::xml_node redact = doc.child("document").child("text").child("redact");
	//std::cout << redact.text().get() << std::endl;


	for (pugi::xml_node redact = text.child("redact"); redact; redact = redact.next_sibling("redact"))
	{
		int replaceLength = 0;
		bool shouldRedact = true;
		for (pugi::xml_attribute attr = redact.first_attribute(); attr; attr = attr.next_attribute())
		{
			std::string name = attr.name();
			std::string value = attr.value();
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
				if (std::end(levels) != std::find(std::begin(levels), std::end(levels), value)) {
					shouldRedact = false;
				}
			}
		}
		if (!shouldRedact) {
			std::cout << "redact:";
			std::cout << redact.text().get();
			std::cout << "you can see me" << std::endl;
			//std::cout << " " << attr.name() << "=" << attr.value() << std::endl;
		}
		else if (shouldRedact) {
			std::cout << "value" << redact.text() << std::endl;
			string redacted = string(replaceLength, 'X');
			char *cstr = new char[replaceLength + 1];
			redacted.copy(cstr, redacted.size() + 1);
			cstr[redacted.size()] = '\0';
			std::cout << replaceLength << std::endl;
			std::cout << cstr << std::endl;
			redact.text().set(cstr);
			std::cout << "value" << redact.text() << std::endl;
		}
	}
	for (pugi::xml_node_iterator it = text.begin(); it != text.end(); ++it)
	{
		std::cout << "text:";
		std::cout << it->text().get();
		fout << it->text().get();
		std::cout << std::endl;
	}
	fout.close();
	//std::cout << "Document:\n";
	//doc.save(std::cout);
	//std::cout << std::endl;
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
