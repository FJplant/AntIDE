echo
echo Ant batch file
echo Antsoft Co.
echo 
ANT_HOME=/usr/antdev
JDK_HOME=/usr/Java/jdk1.1
CLASSPATH=$JDK_HOME/lib/classes.zip
CLASSPATH=$CLASSPATH:$ANT_HOME/lib/ant.jar:$ANT_HOME/lib/swingall.jar:$ANT_HOME/lib/jh.jar
export ANT_HOME JDK_HOME CLASSPATH
$JDK_HOME/bin/java -classpath $CLASSPATH -mx30m -Dlax.dir=$ANT_HOME com.antsoft.ant.main.Main
