#include "Tree.h"


Tree* TR_init() {
	return NULL;
}
void TR_free(Tree** list) {
	// Source: http://codereview.stackexchange.com/a/46822
	Tree *tmp, *old;
	for (tmp = *list; tmp != NULL && tmp->left != NULL; tmp = tmp->left);

	while ( *list != NULL) {
		for (tmp->left = (*list)->right; tmp != NULL && tmp->left != NULL; tmp = tmp->left);
		
		old = *list;
		*list = (*list)->left;
		free(old);
	}
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
	Tree* tmp = NULL;
	while ( *p != NULL && (*p)->key != key ) {
		if ((*p)->key > key) {
			tmp = *p;
			p = &((*p)->left);
		}
		else if ((*p)->key < key) {
			tmp = *p;
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
size_t TR_Size(Tree* list) {
	int node = 0;

	// Morris pre-order algorithme
	Tree* c = list;
	Tree* p = list;

	while (c != NULL) {
		if (c->left == NULL) {
			node++;
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
				node++;
				c = c->right;
			}
		}
	}

	return sizeof(Tree)*node;
}
