package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@ManagedBean(name = "logBean", eager = true)
@RequestScoped
public class LogBean implements UserDao {

    String logText;
    String logLevel = "ALL";
    boolean debug;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    @PostConstruct
    public void init() {
        System.out.println("log page");
        debug = getBitServerResource("debug").getRvalue().equals("true");
        try {
            setLogInfo();
        }catch (Exception e){
            LogTool.getLogger().debug("Error set log fail");
        }
    }

    public void setLogInfo() throws IOException {
        String filename = getBitServerResource("logpath").getRvalue()+"bitServer.log";
        FileReader fileReader;
        if(logLevel.equals("ORTHANC")){
            fileReader= new FileReader("/var/log/orthanc/Orthanc.log");

        }else{
            fileReader= new FileReader(filename);
        }

        Scanner scanner = new Scanner(fileReader);
        StringBuilder buf = new StringBuilder();
        while (scanner.hasNextLine()) {
            String bufStr = scanner.nextLine();
            if(logLevel.equals("ALL")|(logLevel.equals("ORTHANC"))){
                buf.insert(0,(bufStr+"\n"));
            }else{
                if (bufStr.contains(logLevel)){
                    buf.insert(0,(bufStr+"\n"));
                }
            }
        }
        fileReader.close();
        logText = buf.toString();
        PrimeFaces.current().ajax().update(":log:textEditor");
    }

    public void clearLog() throws IOException {
        String filename = getBitServerResource("logpath").getRvalue()+"bitServer.log";
        try(FileOutputStream fileOutputStream = new FileOutputStream(filename))
        {
            String buf = " ";
            byte[] buffer = buf.getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException e){
            //LogTool.getLogger().error("Error saveSettingsCustomMode() NetworkSettingsBean: "+e.getMessage());
        }
        setLogInfo();
    }

}
