package ru.bitServer.util;

import ru.bitServer.dao.UserDao;
import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class DeleteWorkListFile implements UserDao, Runnable {
    String[] pathnames;
    @Override
    public void run() {
        int lifeHourCount = Integer.parseInt(getBitServerResource("workListLifeTime").getRvalue());
        File f = new File(getBitServerResource("WorkListPath").getRvalue());
        pathnames = f.list();
        LogTool.getLogger().info("pathnames "+ Arrays.toString(pathnames));
        for (String pathname : pathnames) {
            LogTool.getLogger().info("1 step "+pathname);
            long curTime = new Date().getTime();
            int i = pathname.indexOf(".");
            String buf = pathname.substring(0,i);
            long fileTime;
            try {
                fileTime = Long.parseLong(buf);
                if( (curTime - lifeHourCount * 3600000L) > fileTime ){
                    File file = new File(getBitServerResource("WorkListPath").getRvalue()+"/"+pathname);
                    if(!file.delete()){
                        LogTool.getLogger().error("Error delete wl-file, maybe tomcat not have rules!");
                    }
                }
            }catch (Exception ignored){ }
        }
    }
}