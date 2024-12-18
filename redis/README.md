# Redis

## Redis  Deployment

### Command-line:
###### Redis can be deployed by cloning this repository locally from Git

- Clone the repository
- Logon to Openshift using **oc** commandline tool
- Switch to the correct project/namespace you're targeting

### Redis Stndalone Deployment
###### Delete previous installation if any
- Run the following command for cleanup:
```
oc delete -n 77c02f-tools all,replicaset,svc,deployment,pvc,secret,configmap,sa,RoleBinding -l app=redis
```
###### Deploy Redis
```
oc process -f redis/redis-sa.yaml | oc apply -f -
```

### Redis HA Deployment

### GitHub Actions:
- Goto GitHub Actions Tab
- Run **Create Redis Cluster - DEV**  workflow for DEV environment
- For TEST and PROD, run the workflow ending with TEST and PROD respectively

### Command-line: 
###### Redis HA can be deployed by cloning this repository locally from Git

- Clone the repository
- Logon to Openshift using **oc** commandline tool
- Switch to the correct project/namespace you're targeting

###### Delete previous installation if any
- Run the following command for cleanup:
```
oc delete -n <namespace-env> all,rc,svc,dc,route,pvc,secret,configmap,sa,RoleBinding -l app=redis-ha
```
###### Deploy Redis
```
oc process -f redis/redis-ha.dc.yaml -p REPLICAS=9 | oc apply -f -
```
 
###### Create Redis Cluster
- Once all the pods are running, run the following command to initialize the cluster:

```
oc exec -it redis-ha-0 -- redis-cli --cluster create --cluster-replicas 2 $(oc get pods -l app=redis-ha -o jsonpath='{range.items[*]}{.status.podIP}:6379 {end}') --cluster-yes -a <password>
```
