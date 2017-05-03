#!/bin/bash
echo
echo Ant batch file
echo Antsoft Co.
echo 
export ANT_HOME=/usr/antdev
export JRE_HOME=$ANT_HOME/jre
export CLASSPATH=$JRE_HOME/lib/rt.jar:$JRE_HOME/lib/i18n.jar
export CLASSPATH=$CLASSPATH:$ANT_HOME/lib/ant.jar:$ANT_HOME/lib/swingall.jar:$ANT_HOME/lib/jh.jar
$JRE_HOME/bin/jre -classpath $CLASSPATH -mx30m -Dlax.dir=$ANT_HOME com.antsoft.ant.main.Main
