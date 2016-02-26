#include <stdlib.h>
#include <stdio.h>
#include "SkipList.h"

int main(int argc, char** argv) {
	SkipList list = SK_init(32, 0.25);

	for (int i = 4; i <= 8; i++)
		SK_Insert(list, i, i);

	SK_Delete(list, 6);
	node* found = SK_Search(list, 5);
	node* notfound = SK_Search(list, 6);

	SK_free(list);

	if (!found)
		return 1;
	if (notfound)
		return 1;

#ifdef WIN32
	system("pause");
#endif
	return 0;
}
