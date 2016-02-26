#include "SkipList.h"

SkipList SK_init(int maxElem, float p) {
	SkipList list;

#ifdef DEBUG
	printf("Creating Skip-List\n");
#endif

	list.levelMAX = (int)round(log2(maxElem));
	list.size = 1;
	list.head = createNode(INT_MIN);
	list.tail = createNode(INT_MAX);
	list.p = p;

#ifdef DEBUG
	printf("Done, max level: %d\n", list.levelMAX);
#endif

	return list;
}
void SK_free(SkipList list) {
	free(list.head);
	free(list.tail);
}
int SK_Search(SkipList list, int value) {
#ifdef DEBUG
	printf("Searching for %d in SkipList[%p]\n", value, &list);
	int step = 0;
#endif
	node* x = list.head;
	// On recherche du plus haut, vers le plus bas
	// Puis, on essaye d'aller le plus à droite possible.
	for (int i = list.size; i >= 1; i--) {
#ifdef DEBUG
		step++;
#endif
		while (x->forward[i]->value < value) {
			x = x->forward[i];
#ifdef DEBUG
			step++;
#endif
		}
	}
	// Si l'élement suivant est celui-qu'on cherche, on l'a trouvé.
	x = x->forward[1];
	if (x->value == value) {
#ifdef DEBUG
		printf("Found %d in %d steps", value, step);
#endif
		return 1;
	}
#ifdef DEBUG
	printf("NotFound %d in %d steps", value, step);
#endif
	return 0;
}
int SK_Insert(SkipList list, int value) {
#ifdef DEBUG
	printf("Inserting %d in SkipList[%p]\n", value, &list);
	int step = 0;
#endif
	node** update = (node**)malloc(sizeof(node*)*list.size);
	node* x = list.head;

	// On marque les noeuds pour la mise à jour
	for (int i = list.size; i >= 1; i--) {
#ifdef DEBUG
		step++;
#endif
		while (x->forward[i]->value < value) {
			x = x->forward[i];
#ifdef DEBUG
			step++;
#endif
		}
		update[i] = x;
	}

	// Vérification qu'on ajoute pas un doublon:
	x = x->forward[1];
	if (x->value == value) {
#ifdef DEBUG
		printf("WARNING: %d was already in the list.", value);
#endif
		x->value = value; // ??
	}
	else {
		int level = getRandomLevel(list);
		if (level > list.size) {
			// TODO: 
#ifdef DEBUG
			step++;
#endif
		}
		
		x = createNode(value);
		for (int i = 1; i <= level; i++) {
			// ???
			x->forward[i] = update[i]->forward[i];
			update[i]->forward[i] = x;
#ifdef DEBUG
			step++;
#endif
		}
	}
#ifdef DEBUG
	printf("OK %d has been added to list.\n", value);
#endif
	free(update);
#ifdef DEBUG
	printf("Done.\n");
#endif
	return 0;
}
int getRandomLevel(SkipList list) {
	int level = 1;
	// TODO: Fix RAND()
	while (rand() < list.p) {
		level++;
	}
	return MIN(level, list.levelMAX);
}
node* createNode(int value) {
	node* noeud = (node*)malloc(sizeof(node));

	noeud->value = value;

	return noeud;
}