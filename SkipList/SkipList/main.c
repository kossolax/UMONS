#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "SkipList.h"
#include "LinkList.h"

int int_pow(int base, int exp);

int main(int argc, char** argv) {
	srand((int)time(NULL));
	clock_t begin, end;
	int maxTest, maxSize;
	if( argc != 3 ) {
	 	maxTest	= 10;
		maxSize = int_pow(2, 8);
	}
	else {
		maxTest = atoi(argv[1]);
		maxSize = atoi(argv[2]);
	}



	begin = clock();
	for (int i = 1; i <= maxTest; i++) {
		SkipList* list = SK_init(maxSize, 0.5f);
		for (int j = 1; j <= maxSize; j++)
			SK_Insert(list, j, j);
		SK_free(list);
	}
	end = clock();
	printf("%lf\n", (double)(end - begin) / CLOCKS_PER_SEC);

	begin = clock();
	for (int i = 1; i <= maxTest; i++) {
		LinkList* list = LL_init();
		for (int j = 1; j <= maxSize; j++)
			list = LL_Insert(list, j, j);
		list = LL_free(list);
	}
	end = clock();
	printf("%lf\n", (double)(end - begin) / CLOCKS_PER_SEC);
#if _WIN32
	system("pause");
#endif
	return 0;
}
int int_pow(int base, int exp) {
	int result = 1;
	while (exp) {
		if (exp & 1)
			result *= base;
		exp /= 2;
		base *= base;
	}
	return result;
}
