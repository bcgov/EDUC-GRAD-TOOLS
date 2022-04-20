#!/bin/bash
echo $BEARER
while read line
do
  curl --location --request POST 'https://soam-test.apps.silver.devops.gov.bc.ca/auth/admin/realms/master/roles' \
  --header "Authorization: Bearer $BEARER" \
  --header "Content-Type: application/json" \
  --data-raw '$line'
done < grad-roles.dat