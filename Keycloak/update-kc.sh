# IAC script for KC update
# ENVS

KC_BASE_URL=$1
KC_PASSWORD=$2
KC_USERNAME=$3
KC_REALM_ID=$4
SCRIPTS_PATH=$5
KC_TOKEN_URL=$6
TKN_FILE="/tmp/token.txt"
REFRESH_INTERVAL="30"



curl -o roles.sh $SCRIPTS_PATH/grad-roles.dat
curl -o client_scopes.sh $SCRIPTS_PATH/grad-client-scopes.lst
curl -o clients.sh $SCRIPTS_PATH/clients.dat

echo Fetching SOAM initial  token
response=$(curl -s   -w POST \
  -d "client_id=admin-cli" \
  -d "username=$KC_USERNAME" \
  -d "password=$KC_PASSWORD" \
  -d "grant_type=password" \
  "$KC_TOKEN_URL/$KC_REALM_ID/protocol/openid-connect/token")
  TKN=$(echo "$response"  | jq -r '.access_token')
  REFRESH_TOKEN=$(echo "$response"  | jq -r '.refresh_token')
  echo "$TKN" > "$TKN_FILE"

  
echo starting refresh token loop
while true; do
  response=$(curl -s   -w POST \
    -d "client_id=admin-cli" \
    -d "grant_type=refresh_token" \
    -d "refresh_token=$REFRESH_TOKEN" \
    "$KC_TOKEN_URL/$KC_REALM_ID/protocol/openid-connect/token")
    TKN=$(echo "$response"  | jq -r '.access_token')
    REFRESH_TOKEN=$(echo "$response"  | jq -r '.refresh_token')
    echo "$TKN" > "$TKN_FILE"
    sleep "$REFRESH_INTERVAL"
  done &
  REFRESH_PID=$!
  
#Create Roles
echo -e "CREATE Roles \n"
jq -c '.[]' roles.sh | while read -r role; do
  result=$(curl -s  -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/roles" \
  --header "Authorization: Bearer "$(cat "$TKN_FILE")" "  \
  --header "Content-Type: application/json" \
  --data-raw "$role")
   echo -e " Response : $result\n"
done



#Create Scopes
echo -e "CREATE Scopes\n"
jq -c '.[]' client_scopes.sh | while read -r scope; do
  result=$(curl -s  -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/client-scopes" \
  --header "Authorization: Bearer "$(cat "$TKN_FILE")" "  \
  --header "Content-Type: application/json" \
  --data-raw "$scope")
   echo -e "Create scope "$scope"  Response : $result\n"
done



#Create Clients
echo -e "CREATE Clients \n"

jq -c '.[]' clients.sh | while read -r client; do
  result=$(curl -s  -w "%{http_code}"   -X  POST "$KC_BASE_URL/$KC_REALM_ID/clients" \
  --header "Authorization: Bearer "$(cat "$TKN_FILE")" "  \
  --header "Content-Type: application/json" \
  --data-raw "$client")
  echo -e " Response : $result\n"
  default_scopes=$(echo "$client" | jq -r '.defaultClientScopes[]')
  clientId=$(echo "$client" | jq -r '.clientId')

  CLIENT_UUID=$(curl -s -X  GET "$KC_BASE_URL/$KC_REALM_ID/clients" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer "$(cat "$TKN_FILE")" "  \
      | jq '.[] | select(.clientId=="'"$clientId"'")' | jq -r '.id')
  
  echo "$default_scopes"  | while read -r scope; do
    echo "$CLIENT_UUID"
    echo "$scope"
    #PUT /{realm}/clients/{id}/default-client-scopes/{clientScopeId}
    result=$(curl -s  -w "%{http_code}"   -X  PUT "$KC_BASE_URL/$KC_REALM_ID/clients/$CLIENT_UUID/default-client-scopes/$scope" \
    --header "Authorization:  Bearer "$(cat "$TKN_FILE")" "   \
    --header "Content-Type: application/json" \
    )
   echo -e " Response : $result\n"
  done
done 
kill $REFRESH_PID
