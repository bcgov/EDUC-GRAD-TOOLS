This is the tini build for redis cluster. The purpose is to control defunct pids that occur on openshift. tini controls defunt pids and turfs them. The image was built localy and is currently publicly hosted on github.io and is associated with the tools repo. Feel free to create a GH action that controls updates.

How to build and push the image:

Prerequisites:
You will need to be logged into github container registry. 

docker buildx build \
--platform linux/amd64 \
-t ghcr.io/bcgov/educ-grad-tools/tini-redis:{version} \
--push \
.