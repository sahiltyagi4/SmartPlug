#!/bin/sh

echo "going to start raspberry pi web server..."
java -cp "/etc/plug/raspberry_pi/target/SmartPlug-v1-jar-with-dependencies.jar" com.iot.server.WebServer