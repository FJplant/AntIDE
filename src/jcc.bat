set JDK_HOME=D:\Jdk1.1.8
set ANT_HOME=C:\Work\ant
set PATH=%JDK_HOME%\bin;
set ClassPath=.;%JDK_HOME%\lib\classes.zip
set ClassPath=%ClassPath%;..\lib\swingall.jar;..\lib\jh.jar
rem set ClassPath=%ClassPath%;%ANT_HOME%\classes

javac -g -d ..\classes %1 %2 %3 %4 %5