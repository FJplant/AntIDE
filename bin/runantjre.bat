@echo off
rem
rem Ant batch file
rem    Antsoft Co.
rem 
set ANT_HOME=C:\Work\Ant
set JRE_HOME=%ANT_HOME%\jre
set classpath=%JRE_HOME%\lib\rt.jar;%JRE_HOME%\lib\i18n.jar
set classpath=%ClassPath%;%ANT_HOME%\classes
set classpath=%ClassPath%;%ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\swingall.jar
start %JRE_HOME%\bin\jrew.exe -classpath %classpath% -mx30m -Dlax.dir=%ANT_HOME% com.antsoft.ant.main.Main %1 %2 %3 %4 %5 %6 %7 %8 %9
@echo on