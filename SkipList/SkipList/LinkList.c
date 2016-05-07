#include "LinkList.h"

LinkList* LL_init() {
	return NULL;
}
void LL_free(LinkList** list) {
	if (list != NULL) {
		LinkList* p = *list;
		LinkList* q;

		while (p != NULL) {
			q = p;
			p = p->forward;
			free(q);
		}
		free(p);
	}
	return NULL;
}
LinkList* LL_Search(LinkList* list, int key) {
	LinkList* p = list;
	while (p != NULL && key < p->key)
		p = p->forward;

	if ( p->key == key )
		return p;
	return NULL;
}
void LL_Insert(LinkList** list, int key, int value) {
	if (*list == NULL) {
		LinkList* n = (LinkList*)malloc(sizeof(LinkList));
		n->key = key;
		n->value = value;
		(*list) = n;
		(*list)->forward = NULL;
	}
	else {
		LinkList* p = *list;
		while (p->forward != NULL && p->key < key)
			p = p->forward;
		
		if (p->value == key) {
			p->value = value;
		}
		else {
			LinkList* n = (LinkList*)malloc(sizeof(LinkList));
			n->key = key;
			n->value = value;
			n->forward = p->forward;
			p->forward = n;
		}
	}
}
void LL_Delete(LinkList** list, int key) {
	LinkList* p = (*list)->forward;
	LinkList* q = *list;

	while (p->forward != NULL && p->key < key) {
		p = p->forward;
		q = q->forward;
	}
	if( p->key == key ) {
		q->forward = p->forward;
		free(p);
	}
}
int LL_GetValueFromNode(LinkList* noeud) {
	return noeud->value;
}
void LL_Print(LinkList* list) {
	LinkList* p = list;
	while ( p != NULL ) {
		printf("[%2d:%2d]", p->key, p->value);
		if (p->forward)
			printf("-->");
		p = p->forward;
	}
	printf("\n");
}
