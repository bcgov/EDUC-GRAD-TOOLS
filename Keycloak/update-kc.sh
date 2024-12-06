# IAC script for KC update
# ENVS

KC_BASE_URL=$1
KC_PASSWORD=$2
KC_USERNAME=$3
KC_REALM_ID=$4
SCRIPTS_PATH=$5
KC_TOKEN_URL=$6

curl -o roles.sh $SCRIPTS_PATH/grad-roles.dat
echo Fetching SOAM token
TKN=$(curl -s -w \
  -d "client_id=admin-cli" \
  -d "username=$KC_USERNAME" \
  -d "password=$KC_PASSWORD" \
  -d "grant_type=password" \
  "$KC_TOKEN_URL/$KC_REALM_ID/protocol/openid-connect/token" | jq -r '.access_token')


#Create Roles
echo -e "CREATE Roles \n"
echo "$KC_BASE_URL/$KC_REALM_ID/roles"
while read line
do
  result=$(curl -s -v -w "%{http_code}"    -X  POST "$KC_BASE_URL/$KC_REALM_ID/roles" \
  --header "Authorization: Bearer $TKN" \
  --header "Content-Type: application/json" \
  --data-raw "$line")
  echo -e " Response : $result\n"
done < roles.sh 
