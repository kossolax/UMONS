#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>

#define MIN(a,b) (((a)<(b))?(a):(b))
//#define DEBUG

typedef struct LinkList {
	int key;
	int value;
	struct LinkList* forward;
} LinkList;

LinkList* LL_init();
void LL_free(LinkList** list);
LinkList* LL_Search(LinkList* list, int key);
void LL_Insert(LinkList** list, int key, int value);
void LL_Delete(LinkList** list, int key);
int LL_GetValueFromNode(LinkList* noeud);
void LL_Print(LinkList* list);
