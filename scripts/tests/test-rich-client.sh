#!/bin/sh#!/bin/sh
# starts the rich client application 
JARS='../../test-classes/rich-client'
for i in `ls ../../test-lib/*.jar` 
do
	JARS="${JARS}:${i}"
done
java -ea -classpath ${JARS} ch.bbv.dnm.users.client.UserApplication -m direct -o ojb -Djava.util.logging.config.file=logging.properties
