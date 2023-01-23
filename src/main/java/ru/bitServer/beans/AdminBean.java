package ru.bitServer.beans;

import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import java.io.FileReader;


@ManagedBean(name = "adminBean", eager = true)
@RequestScoped
public class AdminBean implements UserDao {

    public String buf = "buffer";
    boolean hasTrouble;
    BitServerUser currentUser;
    String currentUserId;
    String errorTitle;
    String errorText;

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorText() {
        return errorText;
    }

    public boolean isHasTrouble() {
        return hasTrouble;
    }

    @PostConstruct
    public void init(){
        System.out.println("admin page");
        hasTrouble = MainBean.hasTrouble;
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        raidGetInfo();
    }


    public void raidGetInfo(){
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/servicemode");
            proc.waitFor();
            //LogTool.getLogger().info("Admin: "+currentUser.getUid().toString()+" select service mode ");
        }catch (Exception e){
            LogTool.getLogger().error("Error during raidGetInfo()");
        }
        String raidStatusFilePath = getBitServerResource("raidStatusFilePath").getRvalue();

        StringBuilder bufFile = new StringBuilder();
        try(FileReader reader = new FileReader(raidStatusFilePath)) {
            int c;
            while ((c = reader.read()) != -1) {
                bufFile.append((char) c);
            }
        } catch (Exception e) {
            LogTool.getLogger().error("Error of read file init() networkSettingsBean: "+e.getMessage());
        }
        if(bufFile.toString().contains("degraded")){
            hasTrouble = true;
            errorTitle = "RAID STATUS";
            errorText = bufFile.toString();
        }

    }
}
