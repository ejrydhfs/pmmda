#!/bin/sh#!/bin/sh
##script to create the Derby database for the test applications
DATABASE_PATH=../../db/
DATABASE_NAME=testdb
LIB=../../test-lib
JARS="$LIB/derby-10.1.2.1.jar:$LIB/derbytools-10.1.2.1.jar:."
JDBC_DRIVER="-Djdbc.drivers=org.apache.derby.jdbc.EmbeddedDriver"
JDBC_URL="-Dij.connection.myConn=jdbc:derby:${DATABASE_PATH}${DATABASE_NAME};"
OUTPUTFILE="-Dij.outfile=createDerbyDB.txt"
SQL_PATH=../../gendir/orm-server/

echo create the database, the schema, the tables, and the integrity rules
java -cp $JARS $OUTPUTFILE $JDBC_DRIVER ${JDBC_URL}create=true org.apache.derby.tools.ij createSchemaOjbDerby.ddl
java -cp $JARS $OUTPUTFILE $JDBC_DRIVER ${JDBC_URL} org.apache.derby.tools.ij ${SQL_PATH}pmmda-createTablesDerby.sql

## java -cp %CP%; %OUTPUTFILE%2.txt %JDBC_DRIVER% %JDBC_URL% org.apache.derby.tools.ij ${SQL_PATH}pmmda-createIntegrityDerby.sql
