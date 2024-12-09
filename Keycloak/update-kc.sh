# IAC script for KC update
# ENVS

KC_BASE_URL=$1
KC_PASSWORD=$2
KC_USERNAME=$3
KC_REALM_ID=$4
SCRIPTS_PATH=$5
KC_TOKEN_URL=$6


curl -o roles.sh $SCRIPTS_PATH/grad-roles.dat
curl -o client_scopes.sh $SCRIPTS_PATH/grad-client-scopes.lst
curl -o clients.sh $SCRIPTS_PATH/clients.dat
echo Fetching SOAM token
TKN=$(curl -s -v  -w POST \
  -d "client_id=admin-cli" \
  -d "username=$KC_USERNAME" \
  -d "password=$KC_PASSWORD" \
  -d "grant_type=password" \
  "$KC_TOKEN_URL/$KC_REALM_ID/protocol/openid-connect/token" | jq -r '.access_token')


#Create Roles
echo -e "CREATE Roles \n"

while read line
do
  result=$(curl -s -v -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/roles" \
  --header "Authorization: Bearer $TKN" \
  --header "Content-Type: application/json" \
  --data-raw "$line")
  echo -e " Response : $result\n"
done < roles.sh

#Create Client Scopes
echo -e "CREATE Client Scopes\n"
while read CLIENT_SCOPE
do
  #Trim scope if it's more than 36 chars long
  CLIENT_SCOPE_TRIMMED=$CLIENT_SCOPE
  if [ ${#CLIENT_SCOPE} -gt 36 ]; then
    CLIENT_SCOPE_TRIMMED=${CLIENT_SCOPE:0:36}
    echo "Scope Trimmed $CLIENT_SCOPE_TRIMMED"
  fi

   result=$(curl  -s -v -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/client-scopes" \
  --header "Authorization: Bearer $TKN"  \
  --header "Content-Type: application/json" \
  --data-raw "{\"id\": \"$CLIENT_SCOPE_TRIMMED\", \"name\": \"$CLIENT_SCOPE\", \"protocol\": \"openid-connect\", \"attributes\": { \"include.in.token.scope\": \"true\", \"display.on.consent.screen\": \"false\"}}")
  echo -e " Response : $result\n"
done < client_scopes.sh


#Create Clients
echo -e "CREATE Clients \n"

while read line
do
  result=$(curl -s -v -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/clients" \
  --header "Authorization: Bearer $TKN" \
  --header "Content-Type: application/json" \
  --data-raw "$line")
  echo -e " Response : $result\n"
  default_scopes=$(echo "$line" | jq -r '.defaultClientScopes[]')
  CLIENT_UUID=$(curl -s -v   -X  GET "$KC_BASE_URL/$KC_REALM_ID/clients" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $TKN" \
      | jq '.[] | select(.clientId=="'"$CLIENT_ID"'")' | jq -r '.id')
  
  echo "$default_scopes"  | while read -r scope; do
    echo "$CLIENT_UUID"
    echo "$scope"
    #PUT /{realm}/clients/{id}/default-client-scopes/{clientScopeId}
    result=$(curl -s -v -w "%{http_code}"   -X  PUT "$KC_BASE_URL/$KC_REALM_ID/clients/$CLIENT_UUID/default-client-scopes/$scope" \
    --header "Authorization: Bearer $TKN" \
    --header "Content-Type: application/json" \
    )
   echo -e " Response : $result\n"

  done
done < clients.sh
