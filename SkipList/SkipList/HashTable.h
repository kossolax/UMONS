#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>

#include "LinkList.h"

typedef struct HashTable {
	int size;
	struct LinkList** table;
} HashTable;

HashTable* HT_init(int size, float ratio);
void HT_free(HashTable** list);
LinkList* HT_Search(HashTable* list, int key);
void HT_Insert(HashTable* list, int key, int value);
void HT_Delete(HashTable* list, int key);
void HT_Print(HashTable* list);