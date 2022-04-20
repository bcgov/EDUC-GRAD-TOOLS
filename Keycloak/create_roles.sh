#!/bin/bash
# Usage: ./create_roles.sh $URL $BEARER
while read line
do
  curl --write-out '%header{Host} %{response_code}, %{errormsg}' --location --request POST "$1" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "$line"
done < grad-roles.dat