#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>

#define MIN(a,b) (((a)<(b))?(a):(b))
//#define DEBUG

typedef struct SkipList {
	int levelMAX;
	float p;
	int size;
	struct node* head;
} SkipList;
typedef struct node {
	int key;
	int value;
	struct node** forward;

} node;

SkipList* SK_init(int maxElem, float p);
void SK_free(SkipList* list);
node* SK_Search(SkipList* list, int key);
int SK_Insert(SkipList* list, int key, int value);
int SK_Delete(SkipList* list, int key);
int SK_GetValueFromNode(node* noeud);

// Private: eyes only.
node* createNode(SkipList*, int key, int value);
void SK_Print(SkipList* list);
int getRandomLevel(SkipList* list);