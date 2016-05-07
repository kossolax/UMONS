#pragma once
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <time.h>
#include <string.h>

typedef struct sStack {
	void** data;
	size_t dataSize;
	int nbrElement;
	int maxElement;
} SSTACK;

SSTACK* SS_Init(size_t elemSize, int elemMax);
void SS_Push(SSTACK* stack, void* elem);
void* SS_Top(SSTACK* stack, void* data);
void* SS_Get(SSTACK* stack, void* data, int pos);
void* SS_Pop(SSTACK* stack, void* data);
int SS_IsEmpty(SSTACK* stack);
int SS_IsFull(SSTACK* stack);
void SS_free(SSTACK** stack);
void SS_Reset(SSTACK* stack);
int SS_Search(SSTACK* stack, void* val, int(*cmp)(void*, void*));
void delLastSStack(SSTACK* stack);
