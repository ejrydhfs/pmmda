@echo off
REM defines the path to the java virtual machine to use
if not "%JAVA_HOME%" == "" goto JAVA_HOME_SET
set JAVA=java
echo JAVA_HOME IS NOT SET. PLEASE CORRECT YOUR INSTALLATION.
:JAVA_HOME_SET
set JAVA=%JAVA_HOME%\bin\java.exe
REM defines the path to the test libraries and test classes relative to the
REM location of this script file.
set LIB=..\..\test-lib
set CLASSES=..\..\test-classes
"%JAVA%" -ea -classpath %CLASSES%\j2ee-client;%CLASSES%\j2ee-server;%LIB%\commons-beanutils-1.7.0.jar;%LIB%\commons-cli-1.0.jar;%LIB%\commons-collections-3.1.jar;%LIB%\commons-lang-2.1.jar;%LIB%\commons-logging-api-1.0.4.jar;%LIB%\commons-logging-1.0.4.jar;D:\data\projects\pmmda\test-lib\DoG-0.2.2-RC3.jar;%LIB%\flexdock-0.4.0.jar;%LIB%\forms-1.0.5.jar;%LIB%\glazedlists-1.0.0.jar;%LIB%\jcalendar-1.2.2.jar;%LIB%\jbossall-client.jar ch.bbv.dnm.users.client.Application -mode j2ee -Djava.util.logging.config.file=logging.properties
