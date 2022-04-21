#STEPS

##Clone the Repository

##Change directory to Keycloak

##Update script permissions
`chmod 744 grad_rbac_setup.sh`
##SET Env Variables
```
URL=https://soam-test.apps.silver.devops.gov.bc.ca
BEARER=$(curl -k -s -X POST -H 'Content-type: application/x-www-form-urlencoded' \
        -d "client_id=admin-cli&grant_type=password&username=<cli-username>&password=<password>" \
        https://soam-test.apps.silver.devops.gov.bc.ca/auth/realms/master/protocol/openid-connect/token \
        | jq -r '.access_token')
```

##RUN shell script
`./grad_rbac_setup.sh $URL $BEARER`