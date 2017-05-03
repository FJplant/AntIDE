rem
rem Ant batch file
rem    Antsoft Co.
rem 
set JDK_HOME=D:\jdk1.1.8
set path=%JDK_HOME%\bin
set ANT_HOME=C:\Work\Ant
set classpath=%JDK_HOME%\lib\classes.zip;
set classpath=%ClassPath%;%ANT_HOME%\classes
set classpath=%ClassPath%;%ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\swingall.jar;%ANT_HOME%\lib\jh.jar
start java.exe -mx30m -Dlax.dir=%ANT_HOME% com.antsoft.ant.main.Main %1 %2 %3 %4 %5 %6 %7 %8 %9