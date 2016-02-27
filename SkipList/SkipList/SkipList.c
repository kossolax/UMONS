#include "SkipList.h"

SkipList SK_init(int maxElem, float p) {
	static int init = 0;
	SkipList list;

#ifdef DEBUG
	printf("Creating Skip-List\n");
#endif

	if (init == 0) {
		srand((int)time(NULL));
		init = 1;
	}

	list.levelMAX = (int)round(log2(maxElem));
	list.size = 1;
	list.head = createNode(list, INT_MAX, INT_MAX);
	for (int i = 0; i < list.levelMAX; i++)
		list.head->forward[i] = list.head;
	list.p = p;

#ifdef DEBUG
	printf("Done, max level: %d\n", list.levelMAX);
#endif

	return list;
}
void SK_free(SkipList list) {
	free(list.head->forward);
	free(list.head);
}
node* SK_Search(SkipList list, int key) {
#ifdef DEBUG
	printf("Searching for %d in SkipList[%p]\n", key, &list);
	int step = 0;
#endif
	node* x = list.head;
	// On recherche du plus haut, vers le plus bas
	// Puis, on essaye d'aller le plus à droite possible.
	for (int i = list.size - 1; i >= 1; i--) {
#ifdef DEBUG
		step++;
#endif
		while (x->forward[i]->key < key) {
			x = x->forward[i];
#ifdef DEBUG
			step++;
#endif
		}
	}
	// Si l'élement suivant est celui-qu'on cherche, on l'a trouvé.
	x = x->forward[1];
	if (x->key == key) {
#ifdef DEBUG
		printf("Found %d in %d steps", key, step);
#endif
		return x;
	}
#ifdef DEBUG
	printf("NotFound %d in %d steps", key, step);
#endif
	return NULL;
}
int SK_Insert(SkipList list, int key, int value) {
#ifdef DEBUG
	printf("Inserting %d:%d in SkipList[%p]\n", key, value, &list);
	int step = 0;
#endif
	node** update = (node**)malloc(sizeof(node*)*list.levelMAX);
	node* x = list.head;

	// On marque les noeuds pour la mise à jour
	for (int i = list.size - 1; i >= 0; i--) {
#ifdef DEBUG
		step++;
#endif
		while (x->forward[i]->key < key) {
#ifdef DEBUG
			step++;
#endif
			x = x->forward[i];
		}
		update[i] = x;
	}

	// Vérification qu'on ajoute pas un doublon:
	x = x->forward[0];
	if (x->key == key) {
#ifdef DEBUG
		printf("WARNING: KEY:%d was already in the list.", key);
#endif
		x->value = value;
	}
	else {
		int level = getRandomLevel(list);
		if (level > list.size) {
#ifdef DEBUG
			printf("New size: %d\n", level);
#endif
			for (int i = list.size; i < level; i++) {
				update[i] = list.head;
#ifdef DEBUG
				step++;
#endif
			}
			list.size = level;
		}
		
		x = createNode(list, key, value);
		for (int i = 0; i < level; i++) {
			// ???
			x->forward[i] = update[i]->forward[i];
			update[i]->forward[i] = x;
#ifdef DEBUG
			step++;
#endif
		}
	}
#ifdef DEBUG
	printf("OK %d:%d has been added to list in %d steps.\n", key, value, step);
#endif
	free(update);
#ifdef DEBUG
	printf("Done.\n");
#endif
	return 0;
}
int SK_Delete(SkipList list, int key) {
#ifdef DEBUG
	printf("Removing %d in SkipList[%p]\n", key, &list);
	int step = 0;
#endif
	node** update = (node**)malloc(sizeof(node*)*list.size);
	node* x = list.head;

	// On marque les noeuds pour la mise à jour
	for (int i = list.size - 1; i >= 0; i--) {
#ifdef DEBUG
		step++;
#endif
		while (x->forward[i]->key < key) {
#ifdef DEBUG
			step++;
#endif
			x = x->forward[i];
		}
		update[i] = x;
	}

	// La clé t'elle été trouvée ?
	x = x->forward[0];
	if (x->key == key) {
		for (int i = 0; i < list.size; i++) {
			if( update[i]->forward[i] != x)
				continue;
			update[i]->forward[i] = x->forward[i];
#ifdef DEBUG
			step++;
#endif
		}
		free(x->forward);
		free(x);
		while(list.size > 1 && list.head->forward[list.size]->key == INT_MAX )
			list.size--;
	}
#ifdef DEBUG
	printf("OK %d has been added to list in %d steps.\n", key, step);
#endif
	free(update);
#ifdef DEBUG
	printf("Done.\n");
#endif
	return 0;
}
int getRandomLevel(SkipList list) {
	int level = 1;
	while ( (double)rand()/(double)RAND_MAX < (double)list.p) {
		level++;
	}
	return MIN(level, list.levelMAX);
}
node* createNode(SkipList list, int key, int value) {
	node* noeud = (node*)malloc(sizeof(node));

	noeud->key = key;
	noeud->value = value;
	noeud->forward = (node**)malloc(sizeof(node*) * (list.levelMAX));

	return noeud;
}
int SK_GetValueFromNode(node* noeud) {
	return noeud->value;
}