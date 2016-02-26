#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#define DEBUG

typedef struct SkipList {
	int levelMAX;
	int size;
	struct node* head;
	struct node* tail;
} SkipList;
typedef struct node {
	int value;
	struct node* forward[];

} node;

SkipList SK_init(int maxElem);
void SK_free(SkipList list);