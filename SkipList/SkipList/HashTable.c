#include "HashTable.h"

HashTable* HT_init(int size, int ratio) {
	HashTable* list = (HashTable*)malloc(sizeof(HashTable));
	
	list->size = size;
	list->ratio = ratio;
	list->table = (LinkList**)malloc(sizeof(LinkList*)*size);
	for( int i = 0; i < size; i++ )
		list->table[i] = NULL;

	return list;
}
void HT_free(HashTable** list) {
	for (int i = 0; i < (*list)->size; i++)
		LL_free(&((*list)->table[i]));
	free((*list)->table);
	free(*list);
	list = NULL;
}
LinkList* HT_Search(HashTable* list, int key) {
	return LL_Search( list->table[key % list->size], key);
}
void HT_Insert(HashTable** list, int key, int value) {
	LinkList* ptr = (*list)->table[key % (*list)->size];
	int position = LL_Insert( &ptr, key, value);
	(*list)->table[key % (*list)->size] = ptr;

	if (position > (*list)->ratio ) {
		*list = HT_Resize(*list);
	}
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
HashTable* HT_Resize(HashTable* list) {
	LinkList* ptr;
	HashTable* list2 = (HashTable*)HT_init(list->size * 2, list->ratio);

	for (int i = 0; i < list->size; i++) {		
		while( list->table[i] != NULL ) {
			ptr = list->table[i];
			HT_Insert(&list2, ptr->key, ptr->value );
			list->table[i] = ptr->forward;
			free(ptr);
		}
	}

	HT_free(&list);
	return list2;
}