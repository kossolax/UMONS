#include <stdlib.h>
#include <stdio.h>
#include "SkipList.h"

int main(int argc, char** argv) {

	SkipList list = SK_init(32, 0.25);
	SK_free(list);

	system("pause");
	return 0;
}
