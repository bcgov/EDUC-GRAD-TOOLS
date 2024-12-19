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
  



#Create Clients
echo -e "CREATE Clients \n"

jq -c '.[]' roles.sh | while read -r client; do
  
  clientId=$(echo "$client" | jq -r '.name')
  CLIENT_UUID=$(curl -s -X  GET "$KC_BASE_URL/$KC_REALM_ID/roles" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer "$(cat "$TKN_FILE")" "  \
      | jq '.[] | select(.name=="'"$clientId"'")' | jq -r '.id')
  result=$(curl -s  -w "%{http_code}"   -X  DELETE "$KC_BASE_URL/$KC_REALM_ID/roles/$clientId" \
  --header "Authorization: Bearer "$(cat "$TKN_FILE")" "  \
  --header "Content-Type: application/json" )
  
  echo -e " Response client  "$clientId"  create : $result\n"

done 
kill $REFRESH_PID
