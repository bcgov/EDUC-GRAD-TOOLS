#Create Openshift imagestream tags
# Pass the tag name as a command-line parameter
echo -e "TAG Images in Openshift\n"
while read line
do
  oc registry login
  oc project 77c02f-tools
  oc tag "$line":latest "$line":"$1"
  echo -e "\n"
done < grad-apis.lst

oc tag educ-grad-admin-frontend:latest educ-grad-admin-frontend:"$1"
oc tag educ-grad-admin-frontend-static:latest educ-grad-admin-frontend-static:"$1"