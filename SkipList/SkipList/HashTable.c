#include "HashTable.h"

HashTable* HT_init(int size, float ratio) {
	HashTable* list = (HashTable*)malloc(sizeof(HashTable));
	
	list->size = ceil(size * ratio);
	list->table = (LinkList**)malloc(sizeof(LinkList*)*size);
	for( int i = 0; i < size; i++ )
		list->table[i] = NULL;

	return list;
}
void HT_free(HashTable** list) {
	for (int i = 0; i < (*list)->size; i++)
		LL_free(&((*list)->table[i]));
	free(*list);
	list = NULL;
}
LinkList* HT_Search(HashTable* list, int key) {
	return LL_Search( list->table[key % list->size], key);
}
void HT_Insert(HashTable* list, int key, int value) {
	LinkList* ptr = list->table[key % list->size];
	LL_Insert( &ptr, key, value);
	list->table[key % list->size] = ptr;
}
void HT_Delete(HashTable* list, int key) {
	LinkList* ptr = list->table[key % list->size];
	LL_Delete(&ptr, key);
	list->table[key % list->size] = ptr;
}
void HT_Print(HashTable* list) {
	for (int i = 0; i < list->size; i++)
		LL_Print(list->table[i]);
}
