#!/bin/bash
# Usage: ./create_roles.sh $URL $BEARER

echo -e "CREATE Roles\n============URL: $1\n"

while read line
do
  curl --write-out 'URL: %{url_effective}, Response: %{response_code}' --location --request POST "$1/auth/admin/realms/master/roles" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "$line"
  echo -e "\n"
done < grad-roles.dat