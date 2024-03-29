#/bin/bash
# Create Openshift imagestream tags
# Login to Openshift using the CLI login command before you run this script
# ex: oc login --token=sha256~vNdSnB***********tWS9sQ --server=https://api.silver.devops.gov.bc.ca:6443
# Pass the tag name as a command-line parameter
# ex: sh tag-images.sh v1.3.0
printf "\nTAG Images in Openshift\n\n"

oc registry login
oc project 77c02f-tools

while read line
do
  oc tag "$line":latest "$line":"$1"
done < grad-apis.lst

oc project bbe4c3-tools
oc tag educ-grad-admin-frontend:latest educ-grad-admin-frontend:"$1"
oc tag educ-grad-admin-backend:latest educ-grad-admin-backend:"$1"
