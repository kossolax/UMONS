#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>

#define MIN(a,b) (((a)<(b))?(a):(b))
#define DEBUG

typedef struct SkipList {
	int levelMAX;
	float p;
	int size;
	struct node* head;
	struct node* tail;
} SkipList;
typedef struct node {
	int value;
	struct node* forward[];

} node;

SkipList SK_init(int maxElem, float p);
void SK_free(SkipList list);

node* createNode(int value);
int getRandomLevel(SkipList list);