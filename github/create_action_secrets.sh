#!/bin/bash
# Usage: ./create_action_secrets.sh

#Get Github Token from Openshift secrets
GITHUB_TOKEN=$(oc get secret gh-actions -n 77c02f-tools -o go-template --template="{{.data.GITHUB_TOKEN|base64decode}}")

#Create Secrets
echo -e "CREATE Secrets\n=============="
while read REPO_NAME
do
  PUBKEY_EP="https://api.github.com/repos/bcgov/$REPO_NAME/actions/secrets/public-key"
  RESP=$(curl -s --location --request GET "$PUBKEY_EP" \
  --header "Authorization: Bearer $GITHUB_TOKEN" \
  --header "Content-type: application/json")

  PUBKEY=$(echo $RESP | jq -r .key)
  KEY_ID=$(echo $RESP | jq -r .key_id)

  echo -e "Creating Github Secrets for $REPO_NAME"
  #Loop through the repository secrets list
  while read SECRET_KEY
  do
    echo $SECRET_KEY
    #Get secret value from Openshift
    SECRET_VALUE=$(oc get secret gh-actions -n 77c02f-tools -o go-template --template="{{.data.$SECRET_KEY|base64decode}}")
    #Encrypt secret value using libsodium to save as a Github secret
    ENCRYPTED_VALUE=$(python3 encryptsecret.py $PUBKEY $SECRET_VALUE)
    # PUT Endpoint to create/update a Github secret
    CREATE_SECRET_EP="https://api.github.com/repos/bcgov/$REPO_NAME/actions/secrets/$SECRET_KEY"
    curl -s --location --request PUT "$CREATE_SECRET_EP" \
      --header "Authorization: Bearer $GITHUB_TOKEN" \
      --header "Content-Type: application/json" \
      --data-raw "{\"key_id\": \"$KEY_ID\",\"encrypted_value\":\"$ENCRYPTED_VALUE\"}"
  done < secrets.lst

  # Loop through the envs list
  while read $ENV
  do
    echo $ENV
  done < envs.lst

  echo -e "\n"
done < repos.lst
