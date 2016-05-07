#include "Tree.h"


Tree* TR_init() {
	return NULL;
}
void TR_free(Tree** list) {
	if ( (*list)->left != NULL)
		TR_free(&((*list)->left));
	if ((*list)->right != NULL)
		TR_free(&((*list)->right));
	free(*list);
	list = NULL;
}
Tree* TR_Search(Tree* list, int key) {
	Tree* p = (list);
	while (p != NULL && p->key != key) {
		if (p->key > key) {
			p = (p->left);
		}
		else if (p->key < key) {
			p = (p->right);
		}
	}
	return p;
}
void TR_Insert(Tree** list, int key, int value) {
	Tree** p = (list);
	while ( *p != NULL && (*p)->key != key ) {
		if ((*p)->key > key) {
			p = &((*p)->left);
		}
		else if ((*p)->key < key) {
			p = &((*p)->right);
		}
	}
	if( *p != NULL ) {
		(*p)->value = value;
	}
	else {
		*p = (Tree*)malloc(sizeof(Tree));
		(*p)->key = key;
		(*p)->value = value;
		(*p)->left = NULL;
		(*p)->right = NULL;
	}
}
int TR_GetValueFromNode(Tree* noeud) {
	return noeud->value;
}
void TR_Print(Tree* list) {
	// Morris pre-order algorithme
	Tree* c = list;
	Tree* p = list;

	while (c != NULL) {
		if (c->left == NULL) {
			printf("[%2d:%2d]-->", c->key, c->value);
			c = c->right;
		}
		else {
			p = c->left;
			while (p->right != NULL && p->right != c) {
				p = p->right;
			}
			if (p->right == NULL) {
				p->right = c;
				c = c->left;
			}
			else {
				p->right = NULL;
				printf("[%2d:%2d]-->", c->key, c->value);
				c = c->right;
			}
		}
	}
}
