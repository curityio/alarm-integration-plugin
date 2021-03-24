#!/bin/sh


# Initialize the target folder
TARGET_FOLDER=$IDSVR_ROOT/usr/share/plugins/alarm.handlers
mkdir -p $TARGET_FOLDER
rm $TARGET_FOLDER/*.jar

# Build and deploy the Java plugin
mvn clean install -Dmaven.test.skip=true
cp target/identityserver.plugins.alarmhandler*.jar $TARGET_FOLDER

# Copy dependent libraries, excluding those already present in the identity server
rm target/lib/identityserver.sdk*.jar
rm target/lib/slf4j*.jar
cp target/lib/*.jar $TARGET_FOLDER