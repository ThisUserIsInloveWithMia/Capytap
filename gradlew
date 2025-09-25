#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to locate java
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVA_EXEC="$JAVA_HOME/jre/sh/java"
    else
        JAVA_EXEC="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVA_EXEC" ] ; then
        echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME" >&2
        exit 1
    fi
else
    JAVA_EXEC="java"
    which java >/dev/null 2>&1 || {
        echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
        exit 1
    }
fi

DIR="$(cd "$(dirname "$0")"; pwd)"
CLASSPATH="$DIR/gradle/wrapper/gradle-wrapper.jar"

exec "$JAVA_EXEC" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
