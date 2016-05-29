#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#include "SkipList.h"
#include "LinkList.h"
#include "HashTable.h"
#include "Tree.h"

#include "MemoryDump.h"

#define maxPTest	11
float pTest[maxPTest] = { 4 / 5.0f, 3 / 4.0f, 2 / 3.0f, 3 / 5.0f, 1 / 2.0f, 2 / 5.0f, 1 / 3.0f, 1 / 4.0f, 1 / 5.0f, 1 / 10.0f, 1 / 20.0f };

void compteurDeTaille(int maxTest, int maxSize);
void compareAll(int maxTest, double maxTime);
void calculeDePerf(int maxTest, int maxSize);
void clean_stdin();

int main(int argc, char** argv) {
	srand((int)time(NULL));
	
	double maxTime;
	int maxTest, maxSize;

	char c;
	setbuf(stdout, NULL);

	do {
		
		printf(" 1. Variation de p, calcule des hauteurs\n");
		printf(" 2. Variation de p, calcule de performance\n");
		printf(" 3. Comparaisons avec d'autres structures\n");
		printf("\tQuel est le test a effectuer ? ");
		scanf("%c", &c);

		if( c == '1' || c == '2' || c == '3' ) {
			do {
				printf("\tNombre de test a effectuer (min 1)? ");
				scanf("%d", &maxTest);
			} while (maxTest < 1);
		}
		
		if( c == '1' || c == '2' ) {
			do {
				printf("\tCombien d'element a inserer (min 4)? ");
				scanf("%d", &maxSize);
			} while (maxSize < 4);
		}

		if( c == '3' ) {
			printf("\tQuel est la duree maximum du test? ");
			scanf("%lf", &maxTime);
		}
		
		printf("--------------------------------------------------------------\n");
		switch (c) {
			case '1':
				compteurDeTaille(maxTest, maxSize);
				break;
			case '2':
				calculeDePerf(maxTest, maxSize);
				break;
			case '3':
				compareAll(maxTest, maxTime);
				break;
		}

		printf("--------------------------------------------------------------\n");
		printf("Appuyer sur entrer pour continuer, q pour quitter le programme\n");
		clean_stdin();
		scanf("%c", &c);
#if _WIN32
		system("cls");
#else
		system("pause");
#endif
	} while (c != 'q');

	return 0;
}

void compteurDeTaille(int maxTest, int maxSize) {
	int i, j, k;
	int maxLevel = 14;
	unsigned int level[14];

	printf("     p     |");
	for (j = 0; j < maxLevel; j++)
		printf("%8d |", j);
	printf("\n");

	for (i = 0; i < maxPTest; i++) {
		for (j = 0; j < maxLevel; j++)
			level[j] = 0;

		printf("%10.8f |", pTest[i]);

		for (k = 1; k <= maxTest; k++) {
			SkipList* list = SK_init(maxSize, pTest[i]);
			for (j = 0; j < maxSize; j++)
				SK_Insert(list, j, j);

			SK_countNode(list, level, maxLevel);
			SK_free(&list);
		}

		for (j = 0; j < maxLevel; j++)
			printf("%8.2f |", level[j] / (float)maxTest);
		printf("\n");
	}
}
void calculeDePerf(int maxTest, int maxSize) {
	int i, j, k;
	clock_t begin;
	double timer;
	

	printf("     p     | TIME\n");

	for (i = 0; i < maxPTest; i++) {

		printf("%10.8f |", pTest[i]);

		begin = clock();
		for (k = 1; k <= maxTest; k++) {
			SkipList* list = SK_init(maxSize, pTest[i]);
			for (j = 0; j < maxSize; j++)
				SK_Insert(list, j, j);

			SK_free(&list);
		}
		timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
		printf("%12.6f\n", timer);
	}
}
void compareAll(int maxTest, double maxTime) {
	int i, j, k, timeout[5] = { 0, 0, 0, 0 }, maxSize = 1, maxIteration = 24;
	clock_t begin;
	double timer;
	int** keys;
	size_t maxMemoryUsage = 0, memory = 0;

	printf("%13s | %25s | %25s | %25s | %25s |\n", "operations", "SkipList         ", "HashTable        ", "BinaryTree         ", "LinkList         ");
	printf("%13s | %25s | %25s | %25s | %25s |\n", "iter.   elems", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ");

	for (k = 1; k <= maxIteration; k++) {
		maxSize *= 2;
		keys = (int**)malloc(sizeof(int*) * maxTest);
		for (i = 0; i < maxTest; i++) {
			keys[i] = (int*)malloc(sizeof(int) * maxSize);
			for (j = 0; j < maxSize; j++)
				keys[i][j] = rand() % (j + 1);
		}

		printf("%3d %9d | ", maxTest, maxSize);

		if (timeout[0] == 0) {
			begin = clock();
			for (i = 0; i < maxTest; i++) {
				SkipList* list = SK_init(maxSize, 0.5f);
				for (j = 0; j < maxSize; j++)
					SK_Insert(list, keys[i][j], j);
				memory = getCurrentRSS();
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				SK_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[0] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT-  | ");
		}


		maxMemoryUsage = 0;
		if (timeout[1] == 0) {
			begin = clock();
			for (i = 0; i < maxTest; i++) {
				HashTable* list = HT_init(128, 4);
				for (j = 0; j < maxSize; j++)
					HT_Insert(&list, keys[i][j], j);
				memory = getCurrentRSS();
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				HT_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[1] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT-  | ");
		}



		maxMemoryUsage = 0;
		if (timeout[2] == 0) {
			begin = clock();
			for (i = 0; i < maxTest; i++) {
				Tree* list = TR_init();
				for (j = 0; j < maxSize; j++)
					TR_Insert(&list, keys[i][j], j);
				memory = getCurrentRSS();
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				TR_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[2] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT-  | ");
		}


		maxMemoryUsage = 0;
		if (timeout[3] == 0) {
			begin = clock();
			for (i = 0; i < maxTest; i++) {
				LinkList* list = LL_init();
				for (j = 0; j < maxSize; j++)
					LL_Insert(&list, keys[i][j], j);
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				LL_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[3] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT-  | ");
		}

		printf("\n");

		for (i = 0; i < maxTest; i++)
			free(keys[i]);
		free(keys);

		if (timeout[0] == 1 && timeout[1] == 1 && timeout[2] == 1 && timeout[3] == 1 )
			break;
	}
}
void clean_stdin(void) {
	int c;
	do {
		c = getchar();
	} while (c != '\n' && c != EOF);
}