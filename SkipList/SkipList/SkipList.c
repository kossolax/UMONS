#include "SkipList.h"

//#define USE_CSTD


//---------------BEGINSKInit----------------
SkipList* SK_init(int maxElem, float p) {
	SkipList* list = (SkipList*)malloc(sizeof(SkipList));
#ifdef DEBUG
	printf("Creating Skip-List\n");
#endif

	list->levelMAX = (int)round(log10(maxElem) / log10(1.0 / (double)p));
	if( list->levelMAX <= 0)
		list->levelMAX = 1;

	list->level = 1;
	list->head = createNode(list, INT_MAX, INT_MAX);
	for (int i = 0; i < list->levelMAX; i++)
		list->head->forward[i] = list->head;
	list->p = p;
#ifdef DEBUG
	printf("Done, max level: %d\n", list->levelMAX);
#endif
	return list;
}
//---------------ENDSKInit----------------
void SK_free(SkipList** list) {
	node* p = (*list)->head->forward[0];
	node* q;
	while (p != (*list)->head) {
		q = p->forward[0];
		free(p->forward);
		free(p);
		p = q;
	}
	free((*list)->head->forward);
	free((*list)->head);
	free((*list));
	list = NULL;
}
//---------------BEGINSKSearch----------------
node* SK_Search(SkipList* list, int key) {
#ifdef DEBUG
	printf("Searching for %d in SkipList[%p]\n", key, &list);
	int step = 0;
#endif
	node* x = list->head;
	/* On recherche du plus haut, vers le plus bas
	 * Puis, on essaye d'aller le plus � droite possible. */
	for (int i = list->level - 1; i >= 0; i--) {
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
	/* Si l'�lement suivant est celui-qu'on cherche, on l'a trouv�. */
	x = x->forward[0];
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
//---------------ENDSKSearch----------------
//---------------BEGINSKInsert----------------
int SK_Insert(SkipList* list, int key, int value) {
#ifdef DEBUG
	printf("Inserting %d:%d in SkipList[%p]\n", key, value, list);
	int step = 0;
#endif
	node** update = (node**)malloc(sizeof(node*)*list->levelMAX);
	node* x = list->head;
	/* On marque les noeuds pour la mise � jour */
	for (int i = list->level - 1; i >= 0; i--) {
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
	/* V�rification qu'on ajoute pas un doublon */
	x = x->forward[0];
	if (x->key == key) {
#ifdef DEBUG
		printf("WARNING: KEY:%d was already in the list.", key);
#endif
		x->value = value;
	}
	else {
		int level = getRandomLevel(list);
		if (level > list->level) {
#ifdef DEBUG
			printf("New size: %d\n", level);
#endif
			for (int i = list->level; i < level; i++) {
				update[i] = list->head;
#ifdef DEBUG
				step++;
#endif
			}
			list->level = level;
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
	return 0;
}
//---------------ENDSKInsert----------------
//---------------BEGINSKDelete----------------
int SK_Delete(SkipList* list, int key) {
#ifdef DEBUG
	printf("Removing %d in SkipList[%p]\n", key, &list);
	int step = 0;
#endif
	node** update = (node**)malloc(sizeof(node*)*list->level);
	node* x = list->head;
	/* On marque les noeuds pour la mise � jour */
	for (int i = list->level - 1; i >= 0; i--) {
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

	/* La cl� t'elle �t� trouv�e ? */
	x = x->forward[0];
	if (x->key == key) {
		for (int i = 0; i < list->level; i++) {
			if( update[i]->forward[i] != x)
				break;
			update[i]->forward[i] = x->forward[i];
#ifdef DEBUG
			step++;
#endif
		}
		free(x->forward);
		free(x);
		while(list->level > 1 &&
			list->head->forward[list->level-1] == list->head ) {
			list->level--;
		}
	}
#ifdef DEBUG
	printf("OK %d has been added to list in %d steps.\n", key, step);
#endif
	free(update);
	return 0;
}
//---------------ENDSKDelete----------------
void SK_Print(SkipList* list) {

	node* x;
	node* y;

	printf("--------- List of %d levels ---------------\n", list->level);
	for (int i = list->level - 1; i >= 0; i--) {

		x = list->head;
		printf("[]-->");


		while (x->forward[i] != list->head) {
			if (i != 0) {
				y = x;
				while (y->forward[0] != x->forward[i]) {
					printf("--------->");
					y = y->forward[0];
				}
			}
			x = x->forward[i];
			printf("[%2d,%2d]-->", x->key, x->value);
		}
		if (i != 0) {
			y = x;
			while (y->forward[0] != x->forward[i]) {
				printf("--------->");
				y = y->forward[0];
			}
		}
		printf("[NiL]\n");
	}
	printf("\n\n");
}

static inline double to_double(uint64_t x) {
	const union { uint64_t i; double d; } u = { .i = UINT64_C(0x3FF) << 52 | x >> 12 };
	return u.d - 1.0;
}

//---------------BEGINSKRandom----------------
int getRandomLevel(SkipList* list) {
	int level = 1;
#ifdef USE_CSTD
	while ((double)rand() / (double)RAND_MAX < (double)list->p) {
#else
	while ( to_double(RD_next()) < (double)list->p) {
#endif
		level++;
	}
	return MIN(level, list->levelMAX);
}
//---------------ENDSKRandom----------------

node* createNode(SkipList* list, int key, int value) {
	node* noeud = (node*)malloc(sizeof(node));

	noeud->key = key;
	noeud->value = value;
	noeud->forward = (node**)malloc(sizeof(node*) * (list->levelMAX));

	return noeud;
}
int SK_GetValueFromNode(node* noeud) {
	return noeud->value;
}
void SK_countNode(SkipList* list, unsigned int level[], int maxLevel) {
	if (list->level < maxLevel)
		maxLevel = list->level;

	node* p;
	node* q;
	for (int i = 0; i < maxLevel; i++) {
		p = list->head->forward[i];

		while (p != list->head ) {
			q = p->forward[i];
			p = q;

			level[i]++;
		}
	}
}