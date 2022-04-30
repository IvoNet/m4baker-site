#!/usr/bin/env bash

mvn clean package
if [ "$?" -ne 0 ]; then
    exit 1
fi