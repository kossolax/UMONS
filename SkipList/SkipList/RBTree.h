#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>

// Source: https://github.com/sebastiencs/red-black-tree/blob/master/rbtree.c

#define ISRED(a) ((a) ? (a->color == RED) : (0))
#define RBCMP(a,b) ((a == b) ? (0) : ((a < b) ? (-1) : (1)))


typedef enum RBColor {
	BLACK = 0,
	RED = 1
} RBColor;
typedef struct RBTree {
	int key;
	int value;
	RBColor color;
	struct RBTree* left;
	struct RBTree* right;
} RBTree;


RBTree* RB_init();
void RB_free(RBTree** list);
RBTree* RB_Search(RBTree* list, int key);
void RB_Insert(RBTree** list, int key, int value);
int RB_GetValueFromNode(RBTree* noeud);
RBTree* remove_key(RBTree *node, int key);

size_t RB_Size(RBTree* list);