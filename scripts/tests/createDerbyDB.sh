REM script to create the Derby database for the test applications
set DATABASE_PATH=../../db/
set DATABASE_NAME=testdb
set LIB=../../test-lib/
set CP=%LIB%derby-10.1.2.1.jar;%LIB%derbytools-10.1.2.1.jar;.
set JDBC_DRIVER=-Djdbc.drivers=org.apache.derby.jdbc.EmbeddedDriver
set JDBC_URL=-Dij.connection.myConn=jdbc:derby:%DATABASE_PATH%%DATABASE_NAME%;
set OUTPUTFILE=-Dij.outfile=createDerbyDB.txt
set SQL_PATH=../../gendir/orm-server/

REM create the database, the schema, the tables, and the integrity rules
java -cp %CP%; %OUTPUTFILE%1.txt %JDBC_DRIVER% %JDBC_URL%create=true org.apache.derby.tools.ij createSchemaOjbDerby.ddl
java -cp %CP%; %OUTPUTFILE%1.txt %JDBC_DRIVER% %JDBC_URL% org.apache.derby.tools.ij %SQL_PATH%createTablesDerby.sql
REM java -cp %CP%; %OUTPUTFILE%2.txt %JDBC_DRIVER% %JDBC_URL% org.apache.derby.tools.ij %SQL_PATH%createIntegrityDerby.sql
