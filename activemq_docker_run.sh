#!/usr/bin/env bash

#--detach
docker run --rm -it \
--name="activemq" \
-p 8161:8161 \
-p 61616:61616 \
-p 5672:5672 \
rmohr/activemq:5.15.9-alpine

#-v /home/igor.segodin/GitHub/camel-demo/data/activemq/conf:/opt/activemq/conf \
#-v /home/igor.segodin/GitHub/camel-demo/data/activemq/data:/opt/activemq/data \


#docker cp activemq:/opt/activemq/conf /home/igor.segodin/GitHub/camel-demo/data/activemq/conf
#docker cp activemq:/opt/activemq/data /home/igor.segodin/GitHub/camel-demo/data/activemq/data