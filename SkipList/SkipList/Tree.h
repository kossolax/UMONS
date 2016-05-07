#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>

#define MIN(a,b) (((a)<(b))?(a):(b))
//#define DEBUG

typedef struct Tree {
	int key;
	int value;
	struct Tree* left;
	struct Tree* right;
} Tree;

Tree* TR_init();
void TR_free(Tree** list);
Tree* TR_Search(Tree* list, int key);
void TR_Insert(Tree** list, int key, int value);
int TR_GetValueFromNode(Tree* noeud);
void TR_Print(Tree* list);
