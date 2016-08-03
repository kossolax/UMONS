CFLAGS=-w -std=c99
LDFLAGS=-O2 -lm
EXEC=./list
OBJ=SkipList/SkipList.o SkipList/LinkList.o SkipList/HashTable.o SkipList/RBTree.o SkipList/Tree.o SkipList/xoroshiro128plus.o SkipList/main.o

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
