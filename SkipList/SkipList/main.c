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
	int i, j, k, timeout[5] = { 0, 0, 0, 0 }, maxSize = 1;
	double timer;
	size_t maxMemoryUsage = 0, memory = 0;
	int maxTest = ( argc >= 2 ? atoi(argv[1]) : 1 );
	int maxIteration = ( argc >= 3 ? atoi(argv[2]) : 24 );
	float p = ( argc >= 4 ? atof(argv[3]) : 0.5f );
	double maxTime = (argc >= 5 ? atof(argv[4]) : 60.0);
	setbuf(stdout, NULL);

	printf("%13s | %25s | %25s | %25s | %25s |\n", "operations", "SkipList         ", "HashTable        ", "BinaryTree         ", "LinkList         ");
	printf("%13s | %25s | %25s | %25s | %25s |\n", "it   elems", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ");

	for (k = 1; k <= maxIteration; k++) {
		maxSize *= 2;
		printf("%3d %9d | ", maxTest, maxSize);

		if (timeout[0] == 0) {
			begin = clock();
			for (i = 1; i <= maxTest; i++) {
				SkipList* list = SK_init(maxSize, p);
				for (j = 1; j <= maxSize; j++)
					SK_Insert(list, rand() % j, j);
				
				memory = getCurrentRSS();
				if (memory > maxMemoryUsage ) maxMemoryUsage = memory;
				SK_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[0] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT - | ");
		}


		maxMemoryUsage = 0;
		if (timeout[1] == 0) {
			begin = clock();
			for (i = 1; i <= maxTest; i++) {
				HashTable* list = HT_init(128, 4);
				for (j = 1; j <= maxSize; j++)
					HT_Insert(&list, rand() % j, j);
				memory = getCurrentRSS();
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				HT_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[1] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT - | ");
		}



		maxMemoryUsage = 0;
		if (timeout[2] == 0) {
			begin = clock();
			for (i = 1; i <= maxTest; i++) {
				Tree* list = TR_init();
				for (j = 1; j <= maxSize; j++)
					TR_Insert(&list, rand() % j, j);
				memory = getCurrentRSS();
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				TR_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[2] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT - | ");
		}


		maxMemoryUsage = 0;
		if (timeout[3] == 0) {
			begin = clock();
			for (i = 1; i <= maxTest; i++) {
				LinkList* list = LL_init();
				for (j = 1; j <= maxSize; j++)
					LL_Insert(&list, rand() % j, j);
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				LL_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[3] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT - | ");
		}

		printf("\n");
	}

#if _WIN32
	system("pause");
#endif
	return 0;
}
