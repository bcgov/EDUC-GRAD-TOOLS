#!/bin/bash
# Usage: ./grad_rbac_setup.sh $URL $BEARER

echo -e "Keycloak URL: $1\n"
CREATE_ROLE='/auth/admin/realms/master/roles'
CREATE_CLIENT_SCOPE='/auth/admin/realms/master/client-scopes'
ASSIGN_ROLES_TO_CLIENT_SCOPE='/auth/admin/realms/master/client-scopes/{client-scope}/scope-mappings/realm'

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
while read CLIENT_SCOPE
do
  #Trim scope if it's more than 36 chars long
  CLIENT_SCOPE_TRIMMED=$CLIENT_SCOPE
  if [ ${#CLIENT_SCOPE} -gt 36 ]; then
    CLIENT_SCOPE_TRIMMED=${CLIENT_SCOPE:0:36}
    echo "Scope Trimmed $CLIENT_SCOPE_TRIMMED"
  fi

  curl --write-out 'URL: %{url_effective}, Response: %{response_code}' --location --request POST "$1$CREATE_CLIENT_SCOPE" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "{\"id\": \"$CLIENT_SCOPE_TRIMMED\", \"name\": \"$CLIENT_SCOPE\", \"protocol\": \"openid-connect\", \"attributes\": { \"include.in.token.scope\": \"true\", \"display.on.consent.screen\": \"false\"}}"
  echo -e "\n"
done < grad-client-scopes.lst

#Assign ROLES to client scopes
echo -e "Assign Roles to Client Scopes\n============\nURL: $1$ASSIGN_ROLES_TO_CLIENT_SCOPE\n"

# ADD GRAD_SYSTEM_COORDINATOR Role
ROLE="GRAD_SYSTEM_COORDINATOR"
# Get Role ID by Role
ROLE_ID=$(curl --location --request GET "$1$CREATE_ROLE/$ROLE" --header "Authorization: Bearer $2" | jq -r .id)

while read CLIENT_SCOPE
do
  #Trim scope if it's more than 36 chars long
  CLIENT_SCOPE_TRIMMED=$CLIENT_SCOPE
  if [ ${#CLIENT_SCOPE} -gt 36 ]; then
    CLIENT_SCOPE_TRIMMED=${CLIENT_SCOPE:0:36}
    echo "Scope Trimmed $CLIENT_SCOPE_TRIMMED"
  fi

  # Replace the placeholder with the client-scope value
  URL=$(echo "$1$ASSIGN_ROLES_TO_CLIENT_SCOPE" | sed -e "s|{client-scope}|$CLIENT_SCOPE_TRIMMED|g")

  curl --write-out 'URL: %{url_effective}, Response: %{response_code}' --location --request POST "$URL" \
  --header "Authorization: Bearer $2" \
  --header "Content-Type: application/json" \
  --data-raw "[{\"id\": \"$ROLE_ID\"}]"
  echo -e "\n"
done < grad-client-scopes.lst
