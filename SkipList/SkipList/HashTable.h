#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>

#include "LinkList.h"

typedef struct HashTable {
	int size;
	int ratio;
	LinkList** table;
} HashTable;

HashTable* HT_init(int size, int ratio);
void HT_free(HashTable** list);
LinkList* HT_Search(HashTable* list, int key);
void HT_Insert(HashTable** list, int key, int value);
void HT_Delete(HashTable* list, int key);
void HT_Print(HashTable* list);

// Private: eyes only.
HashTable* HT_Resize(HashTable* list);