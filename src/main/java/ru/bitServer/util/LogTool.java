package ru.bitServer.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class LogTool {

    private static final Logger log =  Logger.getLogger(LogTool.class);
    private static boolean initializationFlag = false;
    private static String fileName;

    private static void initializeLogger(){
        log.setLevel(Level.DEBUG);
        LogTool.setFileName(fileName);
        RollingFileAppender appender = new RollingFileAppender();
        appender.setAppend(true);
        appender.setMaxFileSize("1MB");
        appender.setMaxBackupIndex(1);
        appender.setFile(fileName + "bitServer.log");
        appender.activateOptions();
        PatternLayout layOut = new PatternLayout();
        layOut.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        appender.setLayout(layOut);
        log.addAppender(appender);
    }

    public static Logger getLogger(){
        if(!initializationFlag){
            initializeLogger();
            initializationFlag = true;
        }
        return LogTool.log;
    }

    public static void setFileName(String fileName){
        LogTool.fileName = fileName;
    }
}
