CC = g++
CFLAGS = -g -Wall -std=c++11
SRC = src
INCLUDES = include
TARGET = redaction

all: $(TARGET)

$(TARGET): $(SRC)/$(TARGET).cpp redaction/src/pugixml.cpp
	$(CC) $(CFLAGS) -I $(INCLUDES) -o $@ $^ 

clean:
	rm $(TARGET) *.o