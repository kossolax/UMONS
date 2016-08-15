#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>
#include "xoroshiro128plus.h"

#define MIN(a,b) (((a)<(b))?(a):(b))
//#define DEBUG

//---------------BEGINSKStruct----------------
typedef struct SkipList {
	int levelMAX, level;
	float p;
	struct node* head;
} SkipList;
typedef struct node {
	int key, value, height;
	struct node** forward;
} SKNode;
//---------------ENDSKStruct----------------

SkipList* SK_init(int maxElem, float p);
void SK_free(SkipList** list);
SKNode* SK_Search(SkipList* list, int key);
int SK_Insert(SkipList** list, int key, int value);
int SK_Delete(SkipList* list, int key);
int SK_GetValueFromNode(SKNode* noeud);
void SK_Print(SkipList* list);

// Private: eyes only.
SKNode* createNode(SkipList*, int key, int value, int height);
void removeNode(SKNode* node);
int getRandomLevel(SkipList* list);
void SK_countNode(SkipList* list, unsigned int level[], int maxLevel);
size_t SK_Size(SkipList* list);
