#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#include "SkipList.h"
#include "LinkList.h"
#include "HashTable.h"
#include "Tree.h"

#include "MemoryDump.h"

int main(int argc, char** argv) {
	srand((int)time(NULL));
	clock_t begin;
	int i, j, k, timeout[5] = { 0, 0, 0, 0 }, maxSize = 1000;
	double timer;
	size_t maxMemoryUsage = 0, memory = 0;
	int maxTest = (argc >= 2 ? atoi(argv[1]) : 1000000);
	int maxIteration = (argc >= 3 ? atoi(argv[2]) : 24);
	float p = (argc >= 4 ? atof(argv[3]) : 0.5f);
	double maxTime = (argc >= 5 ? atof(argv[4]) : 10.0);
	int** keys;
	int maxLevel = 14, maxPTest = 11;
	unsigned int level[14];
	float pTest[] = { 4 / 5.0f, 3 / 4.0f, 2 / 3.0f, 3 / 5.0f, 1 / 2.0f, 2 / 5.0f, 1 / 3.0f, 1 / 4.0f, 1 / 5.0f, 1 / 10.0f, 1 / 20.0f };
	setbuf(stdout, NULL);

#if _WIN32
	system("pause");
#endif
	printf("--------------------------------------------------------------------------------\n");
	
	printf("     p     |");
	for (j = 0; j < maxLevel; j++)
		printf("%8d |", j);
	printf(" TIME\n");

	for (i = 0; i < maxPTest; i++) {
		for (j = 0; j < maxLevel; j++)
			level[j] = 0;

		printf("%10.8f |", pTest[i]);

		begin = clock();
		for (k = 1; k <= maxTest; k++) {
			SkipList* list = SK_init(maxSize, pTest[i]);
			for (j = 0; j < maxSize; j++)
				SK_Insert(list, j, j);

			SK_countNode(list, level, maxLevel);
			SK_free(&list);
		}
		timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
		

		k = 0;
		for (j = 0; j < maxLevel; j++)
			printf("%8.2f |", level[j] / (float)maxTest);
		printf("%12.8f\n", timer);
	}
#if _WIN32
	system("pause");
#endif
	printf("--------------------------------------------------------------------------------\n");
	maxSize = 1;
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
				SkipList* list = SK_init(maxSize, p);
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
	}

#if _WIN32
	system("pause");
#endif
	return 0;
}
