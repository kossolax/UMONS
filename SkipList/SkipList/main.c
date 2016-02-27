#include <stdlib.h>
#include <stdio.h>
#include "SkipList.h"

int main(int argc, char** argv) {
	srand((int)time(NULL));

	for (int j = 0; j <= 10000; j++) {
		if (j % 100 == 0)
			printf(".");

		SkipList* list = SK_init(128, 0.25);

		int z = rand() % 64 + 1;
		int test1 = rand() % 128;
		int test2 = rand() % 128;
		if (test2 == test1)
			test2--;


		for (int i = 1; i <= 64; i++) {
			SK_Insert(list, rand() % i, i);

			if (i == z) {
				SK_Insert(list, test1, -99999);
				SK_Insert(list, test2, -99999);
			}
		}
		for (int i = 50; i <= 80; i++)
			SK_Insert(list, rand()%i, i);

		for (int i = 0; i <= 128; i++)
			if (i != test1 && (i == test2 || rand()%4 == 0) )
				SK_Delete(list, i);
		
		node* found = SK_Search(list, test1);
		node* notfound = SK_Search(list, test2);

		if (!found) {
			SK_Print(list);
			SK_free(list);
			printf("ERR: %d was not found but exist\n", test1);
#ifdef WIN32
			system("pause");
#endif
			return 1;
		}
		if (notfound) {
			SK_Print(list);
			SK_free(list);
			printf("ERR: %d was found but doesn't exist\n", test2);
#ifdef WIN32
			system("pause");
#endif
			return 1;
		}

		SK_free(list);
	}

	printf("OK\n");

#ifdef WIN32
	system("pause");
#endif
	return 0;
}