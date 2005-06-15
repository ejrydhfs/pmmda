@echo off
REM defines the path to the java virtual machine to use
if not "%JAVA_HOME%" == "" goto JAVA_HOME_SET
set JAVA=java
echo JAVA_HOME IS NOT SET. PLEASE CORRECT YOUR INSTALLATION.
:JAVA_HOME_SET
set ORBD=%JAVA_HOME%\bin\orbd.exe
REM starts the ORB with the default port 1050. You can define another port but
REM must update the CORBA client and server port.
"%ORBD%" -ORBInitialHost localhost -ORBInitialPort 1050

