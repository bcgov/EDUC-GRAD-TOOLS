# IAC script for KC update
# ENVS

KC_BASE_URL=$1
KC_PASSWORD=$2
KC_USERNAME=$3
KC_REALM_ID=$4
SCRIPTS_PATH=$5
KC_TOKEN_URL=$6
TKN_FILE="/tmp/token.txt"



curl -o roles.sh $SCRIPTS_PATH/grad-roles.dat
curl -o client_scopes.sh $SCRIPTS_PATH/grad-client-scopes.lst
curl -o clients.sh $SCRIPTS_PATH/clients.dat
echo Fetching SOAM initial  token
response=$(curl -s -v  -w POST \
  -d "client_id=admin-cli" \
  -d "username=$KC_USERNAME" \
  -d "password=$KC_PASSWORD" \
  -d "grant_type=password" \
  "$KC_TOKEN_URL/$KC_REALM_ID/protocol/openid-connect/token")
  TKN=$(echo "$response"  | jq -r '.access_token')
  REFRESH_TOKEN=$(echo "$response"  | jq -r '.refresh_token')
  echo "$TKN" > "$TKN_FILE"
  echo "initial refresh token $REFRESH_TOKEN"
  

while true; do
  response=$(curl -s -v  -w POST \
    -d "client_id=admin-cli" \
    -d "grant_type=refresh_token" \
    -d "refresh_token=$REFRESH_TOKEN" \
    "$KC_TOKEN_URL/$KC_REALM_ID/protocol/openid-connect/token")
    TKN=$(echo "$response"  | jq -r '.access_token')
    echo "refresh+ $response \n"
    REFRESH_TOKEN=$(echo "$response"  | jq -r '.refresh_token')
    echo "$TKN" > "$TKN_FILE"
    sleep 30
  done &
  REFRESH_PID=$!
  
#Create Roles
echo -e "CREATE Roles \n"

while read line
do
  result=$(curl -s  -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/roles" \
  --header "Authorization: Bearer  "$(cat "$TKN_FILE")" " \
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

   result=$(curl  -s  -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/client-scopes" \
  --header "Authorization: Bearer "$(cat "$TKN_FILE")"   \
  --header "Content-Type: application/json" \
  --data-raw "{\"id\": \"$CLIENT_SCOPE_TRIMMED\", \"name\": \"$CLIENT_SCOPE\", \"protocol\": \"openid-connect\", \"attributes\": { \"include.in.token.scope\": \"true\", \"display.on.consent.screen\": \"false\"}}")
  echo -e " Response : $result\n"
done < client_scopes.sh


#Create Clients
echo -e "CREATE Clients \n"


jq -c '.[]' clients.sh | while read -r client; do
  result=$(curl -s  -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/clients" \
  --header "Authorization: Bearer "$(cat "$TKN_FILE")"  \
  --header "Content-Type: application/json" \
  --data-raw "$client")
  echo -e " Response : $result\n"
  default_scopes=$(echo "$client" | jq -r '.defaultClientScopes[]')
  clientId=$(echo "$client" | jq -r '.clientId')

  CLIENT_UUID=$(curl -s -X  GET "$KC_BASE_URL/$KC_REALM_ID/clients" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer "$(cat "$TKN_FILE")"  \
      | jq '.[] | select(.clientId=="'"$clientId"'")' | jq -r '.id')
  
  echo "$default_scopes"  | while read -r scope; do
    echo "$CLIENT_UUID"
    echo "$scope"
    #PUT /{realm}/clients/{id}/default-client-scopes/{clientScopeId}
    result=$(curl -s  -w "%{http_code}"   -X  PUT "$KC_BASE_URL/$KC_REALM_ID/clients/$CLIENT_UUID/default-client-scopes/$scope" \
    --header "Authorization: Bearer "$(cat "$TKN_FILE")"  \
    --header "Content-Type: application/json" \
    )
   echo -e " Response : $result\n"
  done
done 
kill $REFRESH_PID
