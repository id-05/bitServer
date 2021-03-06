package ru.bitServer.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import ru.bitServer.dao.UserDao;

public class LogTool implements UserDao {

    private static final Logger log =  Logger.getLogger(LogTool.class);
    private static boolean initializationFlag = false;
    private static String fileName;

    private static void intializeLogger(){
        log.setLevel(Level.DEBUG);
        LogTool.setFileName(fileName);
        RollingFileAppender appender = new RollingFileAppender();
        appender.setAppend(true);
        appender.setMaxFileSize("2MB");
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
            intializeLogger();
            initializationFlag = true;
            return LogTool.log;
        }
        else{
            return LogTool.log;
        }
    }

    public static void setFileName(String fileName){
        LogTool.fileName = fileName;
    }
}
