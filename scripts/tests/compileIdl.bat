@echo off
REM compile the IDL file passed as parameter as a test file and generate the
REM associated java files. The script file is useful to debug CORBA compile
REM problems.
SET IDL_COMPILER=%JAVA_HOME%\bin\idlj
SET ROOT=D:\data\projects\pmmda
"%IDL_COMPILER%" "%ROOT%\src\idl-framework" -i "%ROOT%\gendir\corba-idl" -fall -emitAll -td %ROOT%\gendir\corba-common %ROOT%\gendir\corba-idl\%1.idl" 
