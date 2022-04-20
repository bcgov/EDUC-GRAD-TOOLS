#!/bin/bash
# Usage: ./create_roles.sh $URL $BEARER
while read line
do
  curl --write-out '%{json} %{response_code}' --location --request POST "$1" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "$line"
  echo -e "\n"
done < grad-roles.dat