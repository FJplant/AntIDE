rem
rem Ant batch file
rem    Antsoft Co.
rem 
set JAVA_HOME=D:\jdk1.1.8
set path=%JAVA_HOME%\bin
set ANT_HOME=C:\Work\Ant
set classpath=%JAVA_HOME%\lib\classes.zip;
set classpath=%ClassPath%;%ANT_HOME%\classes;%ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\swingall.jar;%ANT_HOME%\lib\jh.jar
java.exe com.antsoft.ant.debugger.AntDebuggerPanel %1 %2 %3 %4 %5 %6 %7 %8 %9