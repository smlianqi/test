#!/bin/sh
tradePortalPID=0
APP_MODULENAME=chat
APP_MAIN=com.elex.im.module.serverchat.ChatServerStart

APP_LOG_CONFIG=propertiesconfig/$APP_MODULENAME/log4j2.xml
APP_LOG_ASYNC="-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"

APP_JAVA_GC="-XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError"
APP_JAVA_OPTS="-server -Xms512m -Xmx512m -XX:NewSize=64m -XX:MaxNewSize=64m -XX:MetaspaceSize=90m -XX:MaxMetaspaceSize=100m"

getTradeProtalPID(){
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        tradePortalPID=`echo $javaps | awk '{print $1}'`
    else
        tradePortalPID=0
    fi
}
startup(){
    getTradeProtalPID
    echo "==============================================================================================="
    if [ $tradePortalPID -ne 0 ]; then
        echo "$APP_MAIN already started(PID=$tradePortalPID)"
        echo "==============================================================================================="
    else
        echo -n "Starting $APP_MAIN"
        nohup $JAVA_HOME/bin/java -Djava.ext.dirs=.:lib -Dlog4j.configurationFile=$APP_LOG_CONFIG $APP_LOG_ASYNC $APP_JAVA_OPTS $APP_JAVA_GC $APP_MAIN >/dev/null &
        getTradeProtalPID
        if [ $tradePortalPID -ne 0 ]; then
            echo "(PID=$tradePortalPID)...[Success]"
            echo "==============================================================================================="
        else
            echo "[Failed]"
            echo "==============================================================================================="
        fi
    fi
}
startup