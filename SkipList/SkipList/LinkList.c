#include "LinkList.h"

LinkList* LL_init() {
	return NULL;
}
LinkList* LL_free(LinkList* list) {
	if (list != NULL) {
		LinkList* p = list;
		LinkList* q;;

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
LinkList* LL_Insert(LinkList* list, int key, int value) {

	LinkList* n = (LinkList*)malloc(sizeof(LinkList));
	n->key = key;
	n->value = value;

	if (list == NULL) {
		list = n;
		list->forward = NULL;
	}
	else {
		LinkList* p = list;
		while (p->forward != NULL && p->key < key)
			p = p->forward;
		
		n->forward = p->forward;
		p->forward = n;
	}
	return list;
}
LinkList* LL_Delete(LinkList* list, int key) {
	LinkList* p = list->forward;
	LinkList* q = list;

	while (p->forward != NULL && p->key < key) {
		p = p->forward;
		q = q->forward;
	}
	if( p->key == key ) {
		q->forward = p->forward;
		free(p);
	}
	return list;
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
