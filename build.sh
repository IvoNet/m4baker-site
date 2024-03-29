#!/usr/bin/env bash


mvn clean package
if [ "$?" -ne 0 ]; then
    exit 1
fi

echo "Versioning..."
python3 ./version.py

echo "Tagging version..."
./tag.sh

docker_name=192.168.2.3:5555
image=m4baker
version=$(<./VERSION)

#deploy="false"
deploy="true"
#versioning=false
versioning=true

OPTIONS="$OPTIONS --no-cache"
#OPTIONS="$OPTIONS --force-rm"

echo "Building docker image..."
docker build ${OPTIONS} --platform=linux/arm64 -t $docker_name/${image}:latest .
if [ "$?" -eq 0 ] && [ "${deploy}" == "true" ]; then
    docker push $docker_name/${image}:latest
else
    exit 1
fi

if [ "$versioning" == "true" ]; then
    docker tag $docker_name/${image}:latest $docker_name/${image}:${version}
    if [ "$?" -eq 0 ] && [ ${deploy} == "true" ]; then
        docker push $docker_name/${image}:${version}
        if [ -f "./deploy.sh" ]; then
          ./deploy.sh
        fi
    fi
fi


