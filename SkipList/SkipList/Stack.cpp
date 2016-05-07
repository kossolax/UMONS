#include "Stack.h"

SSTACK* SS_Init(size_t elemSize, int elemMax) {
	SSTACK* stack;
	int ok = 1;

	if ((stack = (SSTACK*)malloc(sizeof(SSTACK)))) {
		stack->dataSize = elemSize;
		stack->nbrElement = 0;
		stack->maxElement = elemMax;

		if ((stack->data = (void**)malloc(sizeof(void*) * elemMax))) {

			for (int i = 0; i<elemMax && ok; i++) {
				if ((stack->data[i] = (void*)malloc(sizeof(void*) * elemSize)))
					ok = 1;
				else
					ok = 0;
			}

			if (!ok) {
				SS_free(&stack);
			}
		}
		else {
			SS_free(&stack);
		}
	}
	else {
		SS_free(&stack);
	}

	return stack;
}
void SS_Push(SSTACK* stack, void* elem) {

	memcpy(stack->data[stack->nbrElement], elem, stack->dataSize);
	stack->nbrElement++;
}
void* SS_Top(SSTACK* stack, void* data) {
	memcpy(data, stack->data[stack->nbrElement - 1], stack->dataSize);
	return data;
}
void* SS_Get(SSTACK* stack, void* data, int pos) {
	memcpy(data, stack->data[pos], stack->dataSize);
	return data;
}
void* SS_Pop(SSTACK* stack, void* data) {
	stack->nbrElement--;
	memcpy(data, stack->data[stack->nbrElement], stack->dataSize);
	return data;
}
int SS_IsEmpty(SSTACK* stack) {
	return stack->nbrElement == 0;
}
int SS_IsFull(SSTACK* stack) {
	return stack->nbrElement == stack->maxElement;
}
void SS_free(SSTACK** stack) {

	if ((*stack)) {
		if ((*stack)->data) {
			for (int i = 0; i<(*stack)->maxElement; i++) {
				free((*stack)->data[i]);
				(*stack)->data[i] = NULL;
			}

			free((*stack)->data);
			(*stack)->data = NULL;
		}
		free(*stack);
		*stack = NULL;
	}
}
void SS_Reset(SSTACK* stack) {
	stack->nbrElement = 0;
}
int SS_Search(SSTACK* stack, void* val, int(*cmp)(void*, void*)) {
	void* data = val;
	int find = 0, i = 0;
	while (i != stack->nbrElement && !find) {
		if ((*cmp)(val, stack->data[i]))
			find = 1;
		else i++;
	}
	return find;
}
void delLastSStack(SSTACK* stack) {
	stack->nbrElement--;
}
