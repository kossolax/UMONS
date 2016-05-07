#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "SkipList.h"
#include "LinkList.h"

int main(int argc, char** argv) {
	srand((int)time(NULL));
	clock_t begin, end;
	int maxTest, maxSize, i, j;
	float p;

	maxTest = ( argc >= 2 ? atoi(argv[1]) : 10 );
	maxSize = ( argc >= 3 ? atoi(argv[2]) : 1024 );
	p = 			( argc >= 4 ? atof(argv[3]) : 0.5f );
	printf("%d\t%d\t", maxTest, maxSize);

	begin = clock();
	for ( i = 1; i <= maxTest; i++) {
		SkipList* list = SK_init(maxSize, p);
		for ( j = 1; j <= maxSize; j++)
			SK_Insert(list, rand() % j, j);
		SK_free(list);
	}
	end = clock();
	printf("%lf\t", (double)(end - begin) / CLOCKS_PER_SEC);

	if( argc < 4 ) {
		begin = clock();
		for ( i = 1; i <= maxTest; i++) {
			LinkList* list = LL_init();
			for ( j = 1; j <= maxSize; j++)
				LL_Insert(&list, rand()%j, j);
			LL_free(&list);
		}
		end = clock();
		printf("%lf\n", (double)(end - begin) / CLOCKS_PER_SEC);
	}

#if _WIN32
	system("pause");
#endif
	return 0;
}
