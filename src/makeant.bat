set JDK_HOME=D:\Jdk1.1.8
set ANT_HOME=C:\Work\ant
set PATH=%JDK_HOME%\bin;

set ClassPath=.;%JDK_HOME%\lib\classes.zip

set ClassPath=%ClassPath%;..\lib\swingall.jar;..\lib\jh.jar

rem set ClassPath=%ClassPath%;%ANT_HOME%\classes


javac -g -depend -d ..\classes com\antsoft\ant\main\Main.java
