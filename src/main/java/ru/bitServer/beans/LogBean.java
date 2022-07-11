package ru.bitServer.beans;

import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@ManagedBean(name = "logBean", eager = true)
@RequestScoped
public class LogBean implements UserDao {

    String logtext;

    public String getLogtext() {
        return logtext;
    }

    public void setLogtext(String logtext) {
        this.logtext = logtext;
    }

    @PostConstruct
    public void init() {
        System.out.println("log page");
        try {
            setLogInfo();
        }catch (Exception e){
            LogTool.getLogger().debug("Error set log fail");
        }
    }

    public void setLogInfo() throws IOException {
        String filename = getBitServerResource("logpath").getRvalue()+"bitServer.log";
        FileReader fr= new FileReader(filename);
        Scanner scan = new Scanner(fr);
        StringBuilder buf = new StringBuilder();
        while (scan.hasNextLine()) {
            buf.insert(0,(scan.nextLine()+"\n"))   ;
        }
        fr.close();
        logtext = buf.toString();
    }

}
