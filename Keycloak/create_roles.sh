#!/bin/bash
# Usage: ./create_roles.sh $URL $BEARER
while read line
do
  RESULT=$(curl -v --location --request POST "$1" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "$line")
  echo $RESULT
done < grad-roles.dat