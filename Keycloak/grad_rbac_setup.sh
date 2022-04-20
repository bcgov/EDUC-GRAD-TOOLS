#!/bin/bash
# Usage: ./grad_rbac_setup.sh $URL $BEARER

echo -e "Keycloak URL: $1\n"
CREATE_ROLE='/auth/admin/realms/master/roles'
CREATE_CLIENT_SCOPE='/auth/admin/realms/master/client-scopes'

#Create Roles
echo -e "CREATE Roles\n============\nURL: $1$CREATE_ROLE\n"
while read line
do
  curl --write-out 'URL: %{url_effective}, Response: %{response_code}' --location --request POST "$1$CREATE_ROLE" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "$line"
  echo -e "\n"
done < grad-roles.dat

#Create Client Scopes
echo -e "CREATE Client Scopes\n============\nURL: $1$CREATE_CLIENT_SCOPE\n"
while read line
do
  curl --write-out 'URL: %{url_effective}, Response: %{response_code}' --location --request POST "$1$CREATE_CLIENT_SCOPE" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "$line"
  echo -e "\n"
done < grad-client-scopes.dat