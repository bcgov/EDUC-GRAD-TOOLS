#!/bin/bash
# Usage: ./create_roles.sh $URL $BEARER
while read line
do
  curl --location --request POST "$1" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "$line"
done < grad-roles.dat