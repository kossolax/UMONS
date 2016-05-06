#!/bin/bash

for i in $(seq 24)
do
	./list 100 $(echo "2^$i" | bc) >> result.log
done
