# IAC script for KC update
# ENVS

KC_BASE_URL=$1
KC_PASSWORD=$2
KC_USERNAME=$3
KC_REALM_ID=$4


echo Fetching SOAM token
TKN=$(curl -s \
  -d "client_id=admin-cli" \
  -d "username=$KC_USERNAME" \
  -d "password=$KC_PASSWORD" \
  -d "grant_type=password" \
  "https://$KC_BASE_URL/$KC_REALM_ID/protocol/openid-connect/token" | jq -r '.access_token')


#Create Roles
echo -e "CREATE Roles \n"
while read line
do
  curl -sX POST "https://$KC_BASE_URL/$KC_REALM_ID/roles \
  --header "Authorization: Bearer $TKN" \
  --header "Content-Type: application/json" \
  --data-raw "$line"
  echo -e "\n"
done < grad-roles.dat
