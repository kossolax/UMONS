#!/bin/bash

re='^[0-9]+$'

while read elem skiplist linklist tree rbtree hashtable
do
  if [[ $elem =~ $re ]] ; then

    factor="$(echo "1/$skiplist"  | bc -l)"
    skiplist=1
    linklist="$(echo -e "$factor*$linklist"  | bc -l)"
    tree="$(echo -e "$factor*$tree"  | bc -l)"
    rbtree="$(echo -e "$factor*$rbtree"  | bc -l)"
    hashtable="$(echo -e "$factor*$hashtable"  | bc -l)"

  fi

  echo -e "$elem\t\t$skiplist\t$linklist\t$tree\t$rbtree\t$hashtable\n"
done < normalized.dat
