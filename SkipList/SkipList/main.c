#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "SkipList.h"
#include "LinkList.h"
#include "HashTable.h"
#include "Tree.h"

int main(int argc, char** argv) {
	srand((int)time(NULL));
	clock_t begin, end;
	int maxTest, maxSize, i, j;
	float p;
	maxSize = 2;
	maxTest = ( argc >= 2 ? atoi(argv[1]) : 1 );
	p = 	  ( argc >= 3 ? atof(argv[2]) : 0.5f );
	printf("iter.\telems\tSkipList\tHashTable\tBinaryTree\tLinkList\n");

	for (int a = 1; a <= 32; a++) {
		maxSize *= 2;
		printf("%d\t%d\t", maxTest, maxSize);

		begin = clock();
		for (i = 1; i <= maxTest; i++) {
			SkipList* list = SK_init(maxSize, p);
			for (j = 1; j <= maxSize; j++)
				SK_Insert(list, rand() % j, j);
			SK_free(&list);
		}
		end = clock();
		printf("%lf\t", (double)(end - begin) / CLOCKS_PER_SEC);

		begin = clock();
		for (i = 1; i <= maxTest; i++) {
			HashTable* list = HT_init(maxSize, 0.8);
			for (j = 1; j <= maxSize; j++)
				HT_Insert(list, rand() % j, j);
			HT_free(&list);
		}
		end = clock();
		printf("%lf\t", (double)(end - begin) / CLOCKS_PER_SEC);

		begin = clock();
		for (i = 1; i <= maxTest; i++) {
			Tree* list = TR_init();
			for (j = 1; j <= maxSize; j++)
				TR_Insert(&list, rand() % j, j);
			TR_free(&list);
		}
		end = clock();
		printf("%lf\t", (double)(end - begin) / CLOCKS_PER_SEC);
			begin = clock();
		for (i = 1; i <= maxTest; i++) {
			LinkList* list = LL_init();
			for (j = 1; j <= maxSize; j++)
				LL_Insert(&list, rand() % j, j);
			LL_free(&list);
		}
		end = clock();
		printf("%lf", (double)(end - begin) / CLOCKS_PER_SEC);

		printf("\n");
	}

#if _WIN32
	system("pause");
#endif
	return 0;
}
