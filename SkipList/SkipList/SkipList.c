#include "SkipList.h"

// Private SkipList functions signatures
node* createNode(int value);

SkipList SK_init(int maxElem) {
	SkipList list;

#ifdef DEBUG
	printf("Creating Skip-List\n");
#endif

	list.levelMAX = (int)round(log2(maxElem));
	list.size = 0;
	list.head = createNode(INT_MIN);
	list.tail = createNode(INT_MAX);

#ifdef DEBUG
	printf("Done, max level: %d\n", list.levelMAX);
#endif

	return list;
}
void SK_free(SkipList list) {
	free(list.head);
	free(list.tail);
}


node* createNode(int value) {
	node* noeud = (node*)malloc(sizeof(node));

	noeud->value = value;

	return noeud;
}