#!/usr/bin/env bash

mvn clean package
if [ "$?" -ne 0 ]; then
    exit 1
fi

docker_name=192.168.2.3:5555
image=m4baker
version=$(<./VERSION)

#deploy="false"
deploy="true"
#versioning=false
versioning=true

OPTIONS="$OPTIONS --no-cache"
#OPTIONS="$OPTIONS --force-rm"

docker build ${OPTIONS} -t $docker_name/${image}:latest .
if [ "$?" -eq 0 ] && [ "${deploy}" == "true" ]; then
    docker push $docker_name/${image}:latest
else
    exit 1
fi

if [ "$versioning" == "true" ]; then
    docker tag $docker_name/${image}:latest $docker_name/${image}:${version}
    if [ "$?" -eq 0 ] && [ ${deploy} == "true" ]; then
        docker push $docker_name/${image}:${version}
    fi
fi
