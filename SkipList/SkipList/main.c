#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#include "SkipList.h"
#include "LinkList.h"
#include "HashTable.h"
#include "Tree.h"
#include "RBTree.h"
#include "GetTimer.h"

#define maxPTest	11
double pTest[maxPTest] = { 4 / 5.0, 3 / 4.0, 2 / 3.0, 3 / 5.0, 1 / 2.0, 2 / 5.0, 1 / 3.0, 1 / 4.0, 1 / 5.0, 1 / 10.0, 1 / 20.0 };

void compteurDeTaille(int maxTest, int maxSize);
void calculeDePerf(int maxTest, int maxSize);
int compareOne(int maxTest, int maxSize, double maxTime, int timedout, int** keys, void* fctInit(int, float), void fctInsert(void**, int, int), void fctFree(void**), size_t fctGetSize(void*), const int a, const float p);
void compareAll(int maxTest, double maxTime, float p, int mode);
int cmp(const void * a, const void * b);
void deltaOne(int maxTest, int maxSize, int** keys, long long** dataTimer, void* fctInit(int, float), void fctInsert(void**, int, int), void fctFree(void**), int a, float p);
void deltaAll(int maxTest, int maxSize, float p);
void clean_stdin(void);
void drawAList(int maxSize, float p);
double Percentile(long long tab[], int N, double pc);

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

	double maxTime;
	float p;
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
				scanf("%f", &p);
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
			SkipList* list = SK_init(maxSize, (float)pTest[i]);
			for (j = 0; j < maxSize; j++)
				SK_Insert(&list, j, j);

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
			SkipList* list = SK_init(maxSize, (float)pTest[i]);
			for (j = 0; j < maxSize; j++)
				SK_Insert(&list, j, j);

			SK_free(&list);
		}
		timer = (double)(clock() - begin) / CLOCKS_PER_SEC;
		printf("%12.6f\n", timer);
	}
}
int compareOne(int maxTest, int maxSize, double maxTime, int timedout, int** keys, void* fctInit(int,float), void fctInsert(void**, int, int), void fctFree(void**), size_t fctGetSize(void*), const int a, const float p) {
	clock_t begin;
	int i, j;
	double timer;
	void* list;
	size_t memory = 0;

	if ( timedout == 0 ) {
		timer = 0;
		for (i = 0; i < maxTest; i++) {
			begin = clock();
			list = fctInit(a, p);
			for (j = 0; j < maxSize; j++)
				fctInsert(&list, keys[i][j], j);

			timer += (double)(clock() - begin) / CLOCKS_PER_SEC;
			memory += fctGetSize(list);
			begin = clock();
			fctFree(&list);
			timer += (double)(clock() - begin) / CLOCKS_PER_SEC;
		}
		
		printf("%12.8f | %10.1f | ", timer, memory/(double)maxTest );
		if (timer > maxTime) timedout = 1;
	}
	else {
		printf("  -TIMEOUT-  | -TIMEOUT-  | ");
	}

	return timedout;
}
void compareAll(int maxTest, double maxTime, float p, int mode) {
	int i, j, k, timeout[5] = { 0, 0, 0, 0, 0}, maxSize = 1, maxIteration = 32;
	int a, b, c;
	int** keys;

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
				case 3: {
					for (j = 0; j < maxSize; j++) keys[i][j] = j;
					for (j = 0; j < maxSize; j++) {
						a = rand() % maxSize;
						b = rand() % maxSize;
						c = keys[i][a];
						keys[i][a] = keys[i][b];
						keys[i][b] = c;
					}
					break;
				}
			}
		}

		printf("%3d %9d | ", maxTest, maxSize);

		timeout[0] = compareOne(maxTest, maxSize, maxTime, timeout[0], keys, SK_init, SK_Insert, SK_free, SK_Size, maxSize, p);
		timeout[1] = compareOne(maxTest, maxSize, maxTime, timeout[1], keys, HT_init, HT_Insert, HT_free, HT_Size, 128, 4.0f);
		timeout[2] = compareOne(maxTest, maxSize, maxTime, timeout[2], keys, TR_init, TR_Insert, TR_free, TR_Size, 0, 0.0f);
		timeout[3] = compareOne(maxTest, maxSize, maxTime, timeout[3], keys, RB_init, RB_Insert, RB_free, RB_Size, 0, 0.0f);
		timeout[4] = compareOne(maxTest, maxSize, maxTime, timeout[4], keys, LL_init, LL_Insert, LL_free, LL_Size, 0, 0.0f);

		printf("\n");

		for (i = 0; i < maxTest; i++)
			free(keys[i]);
		free(keys);

		if (timeout[0] == 1 && timeout[1] == 1 && timeout[2] == 1 && timeout[3] == 1 && timeout[4] == 1 )
			break;
	}
}
int cmp(const void * a, const void * b) {
	if (*(long long int*)a - *(long long int*)b < 0)
		return -1;
	if (*(long long int*)a - *(long long int*)b > 0)
		return 1;
	if (*(long long int*)a - *(long long int*)b == 0)
		return 0;
}
void deltaOne(int maxTest, int maxSize, int** keys, long long** dataTimer, void* fctInit(int, float), void fctInsert(void**, int, int), void fctFree(void**), int a, float p) {
	int i, j;
	long long begin, timer, min, max, tmp;
	double avg, var, tmp2, tmp3, pc[3];

	for (i = 0; i < maxTest; i++) for (j = 0; j < maxSize; j++) dataTimer[i][j] = 0;
	min = max = tmp = 0;
	tmp3 = tmp2 = avg = var = pc[0] = pc[1] = pc[2] = 0.0;

	for (i = 0; i < maxTest; i++) {
		tmp = 0;
		tmp3 = 0.0;
		void* list = fctInit(a, p);
		for (j = 0; j < maxSize; j++) {
			begin = rdtsc();
			fctInsert(&list, keys[i][j], j);
			timer = rdtsc(); timer -= begin;

			dataTimer[i][j] = timer;
			tmp += timer;
		}
		fctFree(&list);

		qsort(dataTimer[i], maxSize, sizeof(long long), cmp);
		min += dataTimer[i][0];
		max += dataTimer[i][maxSize - 1];
		avg += tmp / (double)maxSize;
		pc[0] += Percentile(dataTimer[i], maxSize, 0.05);
		pc[1] += Percentile(dataTimer[i], maxSize, 0.5);
		pc[2] += Percentile(dataTimer[i], maxSize, 0.95);

		tmp2 = (tmp / (double)maxSize);
		for (j = 0; j < maxSize; j++) tmp3 += (tmp2 - (double)dataTimer[i][j])*(tmp2 - (double)dataTimer[i][j]);
		var += sqrt(tmp3 / (double)(maxSize));
		
	}
	printf("%8.1f %12.1f %8.1f %8.1f %8.1f %8.1f %8.1f | ", min / (double)maxTest, max / (double)maxTest, avg / (double)maxTest, var / (double)maxTest, pc[0] / (double)maxTest, pc[1] / (double)maxTest, pc[2] / (double)maxTest);
}
void deltaAll(int maxTest, int maxSize, float p) {
	int i, j, k, a, b, c;
	int** keys;
	long long** dataTimer;

	printf("%13s | %66s | %66s | %66s |\n", "operations", "SkipList                    ", "HashTable                    ", "RedBlackTree                    ");
	printf("%13s | %66s | %66s | %66s |\n", "iter.   elems", "MIN          MAX      AVG      DEV     PC5     PC50     PC95", "MIN          MAX      AVG      DEV     PC5     PC50     PC95", "MIN          MAX      AVG      DEV     PC5     PC50     PC95");

	for (k = 1; k <= 3; k++) {
		keys = (int**)malloc(sizeof(int*) * maxTest);
		dataTimer = (long long**)malloc(sizeof(long long*) * maxTest);

		for (i = 0; i < maxTest; i++) {
			keys[i] = (int*)malloc(sizeof(int) * maxSize);
			dataTimer[i] = (long long*)malloc(sizeof(long long)*maxSize);

			switch (k) {
				case 1:	for (j = 0; j < maxSize; j++) keys[i][j] = j; break;
				case 2:	for (j = 0; j < maxSize; j++) keys[i][j] = rand() % (j + 1); break;
				case 3:	for (j = 0; j < maxSize; j++) {
					for (j = 0; j < maxSize; j++) keys[i][j] = j;
					for (j = 0; j < maxSize; j++) {
						a = rand() % maxSize;
						b = rand() % maxSize;
						c = keys[i][a];
						keys[i][a] = keys[i][b];
						keys[i][b] = c;
					}
					break;
				}
			}
		}

		printf("%3d %9d | ", maxTest, maxSize);

		deltaOne(maxTest, maxSize, keys, dataTimer, SK_init, SK_Insert, SK_free, maxSize, p);
		deltaOne(maxTest, maxSize, keys, dataTimer, HT_init, HT_Insert, HT_free, 128, 4.f);
		deltaOne(maxTest, maxSize, keys, dataTimer, RB_init, RB_Insert, RB_free, 0, 0.f);

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
void drawAList(int maxSize, float p) {
	int j;

	SkipList* list = SK_init(maxSize, p);
	for (j = 0; j < maxSize; j++)
		SK_Insert(&list, rand() % maxSize, j);

	SK_Print(list);
	SK_free(&list);
}
double Percentile(long long tab[], int N, double pc) {
	double n = (N - 1) * pc + 1;
	int k = (int)n;
	double d = n - k;
	return (double)tab[k - 1] + d * (double)(tab[k] - tab[k - 1]);
}