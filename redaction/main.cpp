#include "pugixml.hpp"
#include <iostream>
#include <stdlib.h>

int main()
{
    pugi::xml_document doc;

    pugi::xml_parse_result result = doc.load_file("tree.xml");

    std::cout << "Load result: " << result.description() << ", mesh name: " << doc.child("mesh").attribute("name").value() << std::endl;
    // pugi::xml_document doc;
    // pugi::xml_parse_result result = doc.load_file("xgconsole.xml");
    // if (!result)
    //     return -1;
        
    // pugi::xpath_node_set tools_with_timeout = doc.select_nodes("/Profile/Tools/Tool[@Timeout > 0]");
    
    // for (pugi::xpath_node node: tools_with_timeout)
    // {
    //     pugi::xml_node tool = node.node();
    //     std::cout << "Tool " << tool.attribute("Filename").value() <<
    //         " has timeout " << tool.attribute("Timeout").as_int() << "\n";
    // }
}