#!/bin/sh#!/bin/sh
# defines the path to the java virtual machine to use
set ORBD=${JAVA_HOME}/bin/orbd
# starts the ORB with the default port 1050. You can define another port but
# REM must update the CORBA client and server port.
"%ORBD%" -ORBInitialHost localhost -ORBInitialPort 1050

