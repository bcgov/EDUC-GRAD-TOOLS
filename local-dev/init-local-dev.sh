#!/bin/bash
PROJECT_NAME=grad-support
# set up redis
REDIS_MASTER_NODE_NAME=redis-1-1
# first clean up, remove artifacts
if [ -d redis ]; then
  rm -r redis
fi
# create dirs for cluster
mkdir redis;
for i in $(seq 0 5);
do
  mkdir -p redis/node-$((i+1))/{conf,data};
  echo "port 700$i
cluster-require-full-coverage no
cluster-migration-barrier 1
protected-mode no
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
requirepass secret
masterauth secret" > redis/node-$((i+1))/conf/redis.conf
done
chmod -R 777 redis
# fire up docker
docker compose --project-name "$PROJECT_NAME" up -d
CONTAINER_ID=$(docker ps --all --filter name="$PROJECT_NAME-$REDIS_MASTER_NODE_NAME" --format="{{.ID}}" | head -n 1)
CONTAINER_STATUS=$(docker inspect --format "{{json .State.Status }}" "$CONTAINER_ID")
until [ "$CONTAINER_STATUS" = '"running"' ]
do
  echo "Waiting for container to start..."
  sleep 1
done
echo "Container up, initiating cluster..."
docker exec "$CONTAINER_ID" redis-cli -a secret --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1 --cluster-yes

