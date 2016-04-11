#!/usr/bin/env bash

sed -i "s,\(^wtfdyum.twitter.appId=\),\1$APPID,g" application.properties
sed -i "s,\(^wtfdyum.twitter.appSecret=\),\1$APPSECRET,g" application.properties

java -jar wtfdyum.jar
