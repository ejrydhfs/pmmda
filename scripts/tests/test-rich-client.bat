@echo off
REM defines the path to the java virtual machine to use
if not "%JAVA_HOME%" == "" goto JAVA_HOME_SET
set JAVA=java
echo JAVA_HOME IS NOT SET. PLEASE CORRECT YOUR INSTALLATION.
:JAVA_HOME_SET
set JAVA=%JAVA_HOME%\bin\java.exe
REM defines the path to the test libraries and test classes relative to the
REM location of this script file. The path includes the dependencies to drivers
REM for Derby and MySQL databases. If you only need one of them you can optimize
REM the class path
set LIB=..\..\test-lib
set CLASSES=..\..\test-classes
"%JAVA%" -ea -classpath %CLASSES%\rich-client;%LIB%\antlr-2.7.5.jar;%LIB%\commons-beanutils-1.7.0.jar;%LIB%\commons-cli-1.0.jar;%LIB%\commons-collections-3.1.jar;%LIB%\commons-dbcp-1.1.jar;%LIB%\commons-io-1.2.jar;%LIB%\commons-lang-2.1.jar;%LIB%\commons-logging-1.0.4.jar;%LIB%\commons-logging-1.0.4.jar;%LIB%\commons-pool-1.2.jar;%LIB%\db-ojb-1.0.4.jar;%LIB%\DoG-0.2.2RC4.jar;%LIB%\flexdock-0.4.0.jar;%LIB%\forms-1.0.6.jar;%LIB%\validation-1.1.jar;%LIB%\glazedlists-1.5.0.jar;%LIB%\jcalendar-1.2.2.jar;%LIB%\derby-10.1.2.1.jar;%LIB%\mysql-connector-3.1.12.jar ch.bbv.dnm.users.client.UserApplication -m direct -o ojb -Djava.util.logging.config.file=logging.properties
