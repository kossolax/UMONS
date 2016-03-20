CFLAGS=-Wall -std=c99
LDFLAGS=-O1 -lm
EXEC=./list
OBJ=SkipList/SkipList.o SkipList/main.o

##################################################################

all: ${EXEC}

${EXEC}: ${OBJ}
	gcc -o $@ $^ ${LDFLAGS}

%o: %.c
	gcc $< -c -o $@ ${CFLAGS} 


##################################################################

.PHONY: clean mrproper

clean:
	rm SkipList/*.o

mrproper: clean
	rm ${EXEC}
