package ru.bitServer.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import ru.bitServer.dao.UserDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogTool implements UserDao {

    private static Logger log =  Logger.getLogger(LogTool.class);
    private static boolean initializationFlag = false;
    private static String fileName;

    LogTool(){
        LogTool.setFileName(getBitServerResource("logpath").getRvalue());
    }

    private static void intializeLogger(){
        log.setLevel(Level.DEBUG);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        RollingFileAppender appender = new RollingFileAppender();
        appender.setAppend(true);
        appender.setMaxFileSize("1MB");
        appender.setMaxBackupIndex(1);
        //appender.setFile(fileName + "_" + dateFormat.format(date) + ".log");
        appender.setFile(fileName + "bitServer.log");
        appender.activateOptions();

        PatternLayout layOut = new PatternLayout();
        layOut.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        appender.setLayout(layOut);

        log.addAppender(appender);
    }

    public static Logger getLogger(){
        if(initializationFlag == false){
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
