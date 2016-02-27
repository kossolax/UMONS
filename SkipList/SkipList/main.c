#include <stdlib.h>
#include <stdio.h>
#include "SkipList.h"

int main(int argc, char** argv) {
	SkipList list = SK_init(32, 0.25);

	SK_Insert(list, 1, 1);
	for (int i = 4; i <= 8; i++)
		SK_Insert(list, i, i);

	SK_Delete(list, 4);
	node* found = SK_Search(list, 5);
	node* notfound = SK_Search(list, 6);

	SK_free(list);

	if (!found) {
		printf("ERR: 5 was not found but exist\n");
#ifdef WIN32
		system("pause");
#endif
		return 1;
	}
	if (notfound) {
		printf("ERR: 6 was found but doesn't exist\n");
#ifdef WIN32
		system("pause");
#endif
		return 1;
	}

	printf("OK\n");

#ifdef WIN32
	system("pause");
#endif
	return 0;
}
