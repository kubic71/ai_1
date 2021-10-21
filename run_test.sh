#!/bin/bash

#mvn clean
mvn compile
java -cp target/classes UcsTest
