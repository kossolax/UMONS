#include "RBTree.h"

// Source: https://github.com/sebastiencs/red-black-tree/blob/master/rbtree.c

RBTree* getMin(RBTree *node);
RBTree* balance_me_that(RBTree* node);

void flip_color(RBTree* node) {
	node->color = !(node->color);
	node->left->color = !(node->left->color);
	node->right->color = !(node->right->color);
}
RBTree* rotate_left(RBTree* left) {
	RBTree* right;
	if (!left)
		return NULL;

	right = left->right;
	left->right = right->left;
	right->left = left;
	right->color = left->color;
	left->color = RED;

	return right;
}

RBTree* rotate_right(RBTree *right) {
	RBTree* left;

	if (!right)
		return NULL;
	left = right->left;
	right->left = left->right;
	left->right = right;
	left->color = right->color;
	right->color = RED;

	return left;
}
RBTree* create_node(int key, int value) {
	RBTree* n = (RBTree*)malloc(sizeof(RBTree));

	n->key = key;
	n->value = value;
	n->color = RED;
	n->left = NULL;
	n->right = NULL;
	return n;
}
RBTree* insert_this(RBTree *node, int key, int value) {
	int	 res;

	if (!node)
		return (create_node(key, value));

	res = RBCMP(key, node->key);
	if (res == 0)
		node->value = value;
	else if (res < 0)
		node->left = insert_this(node->left, key, value);
	else
		node->right = insert_this(node->right, key, value);
	if (ISRED(node->right) && !ISRED(node->left))
		node = rotate_left(node);
	if (ISRED(node->left) && ISRED(node->left->left))
		node = rotate_right(node);
	if (ISRED(node->left) && ISRED(node->right))
		flip_color(node);

	return node;
}
RBTree* getMin(RBTree *node) {
	if (!node)
		return NULL;
	while (node->left)
		node = node->left;
	return (node);
}
inline RBTree* balance_me_that(RBTree* node) {
	if (ISRED(node->right))
		node = rotate_left(node);
	if (ISRED(node->left) && ISRED(node->left->left))
		node = rotate_right(node);
	if (ISRED(node->left) && ISRED(node->right))
		flip_color(node);
	return (node);
}
RBTree* move_red_to_left(RBTree* node) {
	flip_color(node);
	if (node && node->right && ISRED(node->right->left)) {
		node->right = rotate_right(node->right);
		node = rotate_left(node);
		flip_color(node);
	}
	return (node);
}

RBTree* move_red_to_right(RBTree* node) {
	flip_color(node);
	if (node && node->left && ISRED(node->left->left)) {
		node = rotate_right(node);
		flip_color(node);
	}
	return (node);
}

RBTree* remove_min(RBTree *node) {
	if (!node)
		return NULL;

	if (!node->left) {
		free(node);
		return NULL;
	}

	if (!ISRED(node->left) && !ISRED(node->left->left))
		node = move_red_to_left(node);
	node->left = remove_min(node->left);

	return balance_me_that(node);
}

RBTree* remove_it(RBTree *node, int key) {
	RBTree		*tmp;

	if (!node)
		return NULL;
	if (RBCMP(key, node->key) == -1)
	{
		if (node->left)
		{
			if (!ISRED(node->left) && !ISRED(node->left->left))
				node = move_red_to_left(node);
			node->left = remove_key(node->left, key);
		}
	}
	else
	{
		if (ISRED(node->left))
			node = rotate_right(node);
		if (!RBCMP(key, node->key) && !node->right)
		{
			free(node);
			return NULL;
		}
		if (node->right)
		{
			if (!ISRED(node->right) && !ISRED(node->right->left))
				node = move_red_to_right(node);
			if (!RBCMP(key, node->key))
			{
				tmp = getMin(node->right);
				node->key = tmp->key;
				node->value = tmp->value;
				node->right = remove_min(node->right);
			}
			else
				node->right = remove_key(node->right, key);
		}
	}
	return (balance_me_that(node));
}

RBTree* remove_key(RBTree *node, int key) {
	node = remove_it(node, key);
	if (node)
		node->color = BLACK;
	return (node);
}

RBTree* RB_init() {
	return NULL;
}
void RB_free(RBTree** list) {
	// Source: http://codereview.stackexchange.com/a/46822
	RBTree *tmp, *old;
	for (tmp = *list; tmp != NULL && tmp->left != NULL; tmp = tmp->left);

	while (*list != NULL) {
		for (tmp->left = (*list)->right; tmp != NULL && tmp->left != NULL; tmp = tmp->left);

		old = *list;
		*list = (*list)->left;
		free(old);
	}
}

RBTree* RB_Search(RBTree* list, int key) {
	int	cmp;

	while (list) {
		if (!(cmp = RBCMP(key, list->key)))
			return list;
		list = ((cmp < 0) ? (list->left) : (list->right));
	}
	return NULL;
}
void RB_Insert(RBTree** list, int key, int value) {
	*list = insert_this(*list, key, value);
	if (*list)
		(*list)->color = BLACK;
}
int RB_GetValueFromNode(RBTree* noeud) {
	return noeud->value;
}