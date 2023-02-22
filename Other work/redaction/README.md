# Redaction
This program is used for redacting text provided in XML formats. This program is created with Visual Studio 2019.  Credits for the PugiXML library that is being used to parse through the XML documents goes to https://github.com/zeux/pugixml . The format that this program expects to receive can be seen in the example file called "example.xml" seen in this repository. It is required that the xml specifies \<document\>,\<text\>, \</text\>, \</document\> and it is expected that the text is contained within these bounds.  

# Runtime arguments

It is expected that at runtime, the program is ran with the arguments 

./redaction input.xml output.txt --clearance SCI,TS,S,CNF
- ./redaction 

    specifies the executable
- input.xml 

    the second runtime specifies the xml document that is to be parsed for redactions
- output.txt 

    specifies the text document file that the redacted copy will be output to
- --clearance 

    This is a flag that tells the software that the runtime arguments following this (seperated by commas) are the levels of clearance that this copy will have access to.  So, if we say SCI, TS, S, CNF, then this means that a copy will be generated that has access to redactions with level SCI or TS or S or CNF.  It is important that you individually list all of the clearance levels that the copy should have access to and that these clearance levels are provided in a format that is consistent with the input.xml redactions. Mismatched abbreviations will default to being redacted when the software is run.

# \<text\>
\<text\> has the following optional attributes:
- visible
    
    Visible specifies whether replacement characters should be displayed, or if the redacted text should simply be removed. By default, visible is set to true.  This means that it will display a string of the corresponding length with the replacement character  for a redacted piece of text in the document.

- char
    
    Char specifies the replacement character that will be used in redaction.  It is important that if you specify a character, that it is only a single character. By default, this is set to be "X".

# \<redact\>
Within the text bounds, it is expected that text that should be redacted is contained within \<redact\> \</redact\>.  \<redact\> has attributes for: 
- type
    "Type" specifies the length of characters that can be any of the following:
    - "Word"  Gives a length 5
    - "Words" Gives a length 11
    - "Sentence" Gives a length 90
    - "Sentences" 
       Gives a length 181
    - "Paragraph" Gives a length 363
    - Alternatively, you can specifiy a length of replacement characters with an integer
    
- reason

  Specifies a reason for the specific redaction.  Useful for auditing and documentation, however, this is not used in this program's redaction process.
  
- level

  This should specify all the levels of security clearance that this specific redacted text is viewable to.  So, for example, if this document is viewable to people with at least secret clearance or higher, you would expect that level = "S, TS, TS/SCI" or something like that.  Abbreviations for clearance do not need to be the same as shown in this readme or in the example.xml, however, the way you specify clearances in runtime arguments must be consistent with how it is specified in the input xml document.

While all of these \<redact\> attributes should be included as part of an attention to detail, if they are not specified, the default attributes will be assumed. The replace length will default to the length of "Word", which is 5, if no type is provided. The reason will simply not exist. And the clearance level will not exist, which means that the text will default to being redacted, no matter what the clearance specified in the runtime arguments was.
