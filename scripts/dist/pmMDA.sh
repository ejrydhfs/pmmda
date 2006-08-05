#!/bin/sh#!/bin/sh
JAVA_OPTS='-ea -Djava.util.logging.config.file=logging.properties'
JARS='.'
for i in `ls ./lib/*.jar` 
do
	JARS="${JARS}:${i}"
done
java $JAVA_OPTS -classpath $JARS ch.bbv.mda.ui.MdaApplication $1 $2 $3
