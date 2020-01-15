#!/usr/bin/env bash


JAVA_OUT=../../src/main/java

proto(){
	echo "-------- Proto begin ... ---------"
	protoc --java_out=${JAVA_OUT} *.proto


	echo "-------- Proto success ... ---------"

}

proto
