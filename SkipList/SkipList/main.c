#include <stdlib.h>
#include <stdio.h>
#include "SkipList.h"

int main(int argc, char** argv) {

	for (int j = 0; j <= 1000; j++) {
		if (j % 100 == 0)
			printf(".");
		
		SkipList* list = SK_init(32, 0.25);

		for (int i = 1; i <= 32; i++)
			SK_Insert(list, i, i);

		SK_Delete(list, 4);
		SK_Delete(list, 6);
		SK_Delete(list, 9);
		for (int i = 11; i <= 24; i++)
			SK_Delete(list, i);
		
		node* found = SK_Search(list, 5);
		node* notfound = SK_Search(list, 6);

		if (!found) {
			SK_Print(list);
			SK_free(list);
			printf("ERR: 5 was not found but exist\n");
#ifdef WIN32
			system("pause");
#endif
			return 1;
		}
		if (notfound) {
			SK_Print(list);
			SK_free(list);
			printf("ERR: 6 was found but doesn't exist\n");
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