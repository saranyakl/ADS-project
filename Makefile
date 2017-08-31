JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -f *.class
	rm -f encoded.bin code_table.txt decoded.txt
%.class : %.java
	$(JAVAC) $<