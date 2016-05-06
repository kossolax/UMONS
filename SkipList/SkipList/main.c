#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "SkipList.h"
#include "LinkList.h"

int main(int argc, char** argv) {
	srand((int)time(NULL));
	clock_t begin, end;
	const int maxTest = 1;
	const int MaxSize = int_pow(2, 16);

	begin = clock();
	for (int i = 1; i <= maxTest; i++) {
		SkipList* list = SK_init(MaxSize, 0.5f);
		for (int j = 1; j <= MaxSize; j++)
			SK_Insert(list, j, j);
		SK_free(list);
	}
	end = clock();
	printf("%lf\n", (double)(end - begin) / CLOCKS_PER_SEC);
	
	begin = clock();
	for (int i = 1; i <= maxTest; i++) {
		LinkList* list = LL_init();
		for (int j = 1; j <= MaxSize; j++)
			list = LL_Insert(list, j, j);
		//LL_Print(list);
		list = LL_free(list);
	}
	end = clock();
	printf("%lf\n", (double)(end - begin) / CLOCKS_PER_SEC);

	system("pause");
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
