#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>

#define MIN(a,b) (((a)<(b))?(a):(b))
//#define DEBUG

//---------------BEGINSKStruct----------------
typedef struct SkipList {
	int levelMAX, level;
	float p;
	struct node* head;
} SkipList;
typedef struct node {
	int key, value;
	struct node** forward;
} node;
//---------------ENDSKStruct----------------

SkipList* SK_init(int maxElem, float p);
void SK_free(SkipList** list);
node* SK_Search(SkipList* list, int key);
int SK_Insert(SkipList* list, int key, int value);
int SK_Delete(SkipList* list, int key);
int SK_GetValueFromNode(node* noeud);
void SK_Print(SkipList* list);

// Private: eyes only.
node* createNode(SkipList*, int key, int value);
int getRandomLevel(SkipList* list);
