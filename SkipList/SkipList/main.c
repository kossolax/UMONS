#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#include "SkipList.h"
#include "LinkList.h"
#include "HashTable.h"
#include "Tree.h"
#include "RBTree.h"
#include "MemoryDump.h"
#include "GetTimer.h"

#define maxPTest	11
double pTest[maxPTest] = { 4 / 5.0, 3 / 4.0, 2 / 3.0, 3 / 5.0, 1 / 2.0, 2 / 5.0, 1 / 3.0, 1 / 4.0, 1 / 5.0, 1 / 10.0, 1 / 20.0 };

void compteurDeTaille(int maxTest, int maxSize);
void compareAll(int maxTest, double maxTime, double p, int mode);
void deltaAll(int maxTest, int maxSize, double p);
void calculeDePerf(int maxTest, int maxSize);
void drawAList(int maxSize, double p);
void clean_stdin();

#ifdef _WIN32
#include <intrin.h>
uint64_t rdtsc() { return __rdtsc(); }
#else
uint64_t rdtsc() {
	unsigned int lo, hi;
	__asm__ __volatile__("rdtsc" : "=a" (lo), "=d" (hi));
	return ((uint64_t)hi << 32) | lo;
}
#endif

int main(int argc, char** argv) {
	srand((int)time(NULL));
	RD_init();

	double maxTime, p;
	int maxTest, maxSize, mode;

	char c;
	setbuf(stdout, NULL);

	do {

		printf(" 1. Variation de p, calcule des hauteurs\n");
		printf(" 2. Variation de p, calcule de performance\n");
		printf(" 3. Comparaisons avec d'autres structures\n");
		printf(" 4. Difference entre les insertions\n");
		printf(" 5. Generer et afficher une skip-list\n");

		printf("\tQuel est le test a effectuer ? ");
		scanf("%c", &c);

		if( c == '1' || c == '2' || c == '3' || c == '4' ) {
			do {
				printf("\tNombre de test a effectuer (min 1)? ");
				scanf("%d", &maxTest);
			} while (maxTest < 1);
		}

		if( c == '1' || c == '2' || c == '4' || c == '5') {
			do {
				printf("\tCombien d'element a inserer (min 4)? ");
				scanf("%d", &maxSize);
			} while (maxSize < 4);
		}

		if( c == '3' ) {
			printf("\tQuel est la duree maximum du test? ");
			scanf("%lf", &maxTime);
		}
		if (c == '3') {
			do {
				printf("\tLes elements doivent etre (1) sequentiel, (2) semi-sequentiel, (3) aleatoire ? ");
				scanf("%d", &mode);
			} while (mode <= 0 || mode > 3);
		}
		if (c == '3' || c == '4' || c == '5') {
			do {
				printf("\tQuel est la probabilite p ? ");
				scanf("%lf", &p);
			} while (p < 0.001 || p > 0.999 );
		}

		printf("--------------------------------------------------------------\n");
		switch (c) {
			case '1':	compteurDeTaille(maxTest, maxSize);		break;
			case '2':	calculeDePerf(maxTest, maxSize);		break;
			case '3':	compareAll(maxTest, maxTime, p, mode);	break;
			case '4':	deltaAll(maxTest, maxSize, p);			break;
			case '5':	drawAList(maxSize, p);					break;
		}

		printf("--------------------------------------------------------------\n");
		printf("Appuyer sur entrer pour continuer, q pour quitter le programme\n");
		clean_stdin();
		scanf("%c", &c);
#if _WIN32
		system("cls");
#else
		system("clear");
#endif
	} while (c != 'q');

	return 0;
}

void compteurDeTaille(int maxTest, int maxSize) {
	int i, j, k;
	int maxLevel = 10;
	unsigned int level[10];

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
			printf("%8.2f |", level[j] / (double)maxTest);
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
void compareAll(int maxTest, double maxTime, double p, int mode) {
	int i, j, k, timeout[6] = { 0, 0, 0, 0 , 0}, maxSize = 1, maxIteration = 32;
	clock_t begin;
	double timer;
	int** keys;
	size_t maxMemoryUsage = 0, memory = 0;

	printf("%13s | %25s | %25s | %25s | %25s | %25s |\n", "operations", "SkipList         ", "HashTable        ", "BinaryTree         ", "RedBlackTree       ", "LinkList         ");
	printf("%13s | %25s | %25s | %25s | %25s | %25s |\n", "iter.   elems", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ", "TIME   |   MEMORY  ");

	for (k = 1; k <= maxIteration; k++) {
		maxSize *= 2;
		keys = (int**)malloc(sizeof(int*) * maxTest);
		for (i = 0; i < maxTest; i++) {
			keys[i] = (int*)malloc(sizeof(int) * maxSize);
			switch (mode) {
				case 1:	for (j = 0; j < maxSize; j++) keys[i][j] = j; break;
				case 2:	for (j = 0; j < maxSize; j++) keys[i][j] = rand() % (j + 1); break;
				case 3:	for (j = 0; j < maxSize; j++) keys[i][j] = rand() % maxSize; break;
			}
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
				RBTree* list = RB_init();
				for (j = 0; j < maxSize; j++)
					RB_Insert(&list, keys[i][j], j);
				if (memory > maxMemoryUsage) maxMemoryUsage = memory;
				RB_free(&list);
			}
			timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
			printf("%12.8f | %10zd | ", timer, maxMemoryUsage);
			if (timer > maxTime) timeout[3] = 1;
		}
		else {
			printf("  -TIMEOUT-  | -TIMEOUT-  | ");
		}

		maxMemoryUsage = 0;
		if (timeout[4] == 0) {
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
			if (timer > maxTime) timeout[4] = 1;
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

void deltaAll(int maxTest, int maxSize, double p) {
	int i, j, k;
	long long begin, timer, maxTimer, maxTimerOverall;
	int** keys;

	printf("%13s | %16s | %16s | %16s |\n", "operations", "SkipList   ", "HashTable   ", "RedBlackTree ");
	printf("%13s | %16s | %16s | %16s |\n", "iter.   elems", "  TIME   ", "  TIME   ", "  TIME   ");

	for (k = 1; k <= 3; k++) {
		keys = (int**)malloc(sizeof(int*) * maxTest);
		for (i = 0; i < maxTest; i++) {
			keys[i] = (int*)malloc(sizeof(int) * maxSize);
			switch (k) {
				case 1:	for (j = 0; j < maxSize; j++) keys[i][j] = j; break;
				case 2:	for (j = 0; j < maxSize; j++) keys[i][j] = rand() % (j + 1); break;
				case 3:	for (j = 0; j < maxSize; j++) keys[i][j] = rand() % maxSize; break;
			}
		}

		printf("%3d %9d | ", maxTest, maxSize);

		maxTimer = maxTimerOverall = 0.0;
		for (i = 0; i < maxTest; i++) {
			SkipList* list = SK_init(maxSize, p);
			for (j = 0; j < maxSize; j++) {
				begin = rdtsc();
				SK_Insert(list, keys[i][j], j);
				timer = rdtsc(); timer -= begin;
				if (timer > maxTimer) maxTimer = timer;
			}
			SK_free(&list);
			maxTimerOverall += maxTimer;
		}
		printf("%16.1f | ", maxTimerOverall/(double)maxTest);


		maxTimer = maxTimerOverall = 0.0;
		for (i = 0; i < maxTest; i++) {
			HashTable* list = HT_init(128, 4);
			for (j = 0; j < maxSize; j++) {
				begin = rdtsc();
				HT_Insert(&list, keys[i][j], j);
				timer = rdtsc(); timer -= begin;
				if (timer > maxTimer) maxTimer = timer;
			}
			HT_free(&list);
			maxTimerOverall += maxTimer;
		}
		printf("%16.1f | ", maxTimerOverall/(double)maxTest);


		maxTimer = maxTimerOverall = 0.0;
		for (i = 0; i < maxTest; i++) {
			RBTree* list = RB_init(maxSize, p);
			for (j = 0; j < maxSize; j++) {
				begin = rdtsc();
				RB_Insert(&list, keys[i][j], j);
				timer = rdtsc(); timer -= begin;
				if (timer > maxTimer) maxTimer = timer;
			}
			RB_free(&list);
			maxTimerOverall += maxTimer;
		}
		printf("%16.1f | ", maxTimerOverall/(double)maxTest);

		printf("\n");

		for (i = 0; i < maxTest; i++)
			free(keys[i]);
		free(keys);
	}
}
void clean_stdin(void) {
	int c;
	do {
		c = getchar();
	} while (c != '\n' && c != EOF);
}
void drawAList(int maxSize, double p) {
	int j;

	SkipList* list = SK_init(maxSize, p);
	for (j = 0; j < maxSize; j++)
		SK_Insert(list, rand() % maxSize, j);

	SK_Print(list);
	SK_free(&list);
}
