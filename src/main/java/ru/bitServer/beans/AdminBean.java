package ru.bitServer.beans;

import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import java.io.FileReader;
import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "adminBean", eager = true)
@RequestScoped
public class AdminBean implements UserDao {

    public String buf = "buffer";
    boolean hasTrouble;
    BitServerUser currentUser;
    String currentUserId;
    String errorText;

    public String getErrorText() {
        return errorText;
    }

    public boolean isHasTrouble() {
        return hasTrouble;
    }

    @PostConstruct
    public void init(){
        DicomCreatorBean.onUpdate();
        TagEditorBean.onClearForm();
        hasTrouble = MainBean.hasTrouble;
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        errorText = "";
        raidGetInfo();
        orthancGetInfo();
    }

    public void orthancGetInfo(){
        try{
            OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
            StringBuilder stringBuilder = connection.makeGetConnectionAndStringBuilder("/statistics");
            if(!stringBuilder.toString().contains("error")){
                errorText = errorText + "\n"+ "\n" + "\n"+"ORTHANC STATUS GOOD" + "\n" + "\n";
            }else{
                errorText = errorText + "\n"+ "\n" + "\n"+"ORTHANC STATUS FAILURE" + "\n" + "\n";
                hasTrouble = true;
            }
        }catch (Exception e){
            LogTool.getLogger().error("Error of orthancGetInfo: "+e.getMessage());
            hasTrouble = true;
        }
    }

    public void raidGetInfo(){
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/raidinfo");
            proc.waitFor();
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

            if(bufFile.toString().contains("degraded")){
                hasTrouble = true;
                errorText = "RAID STATUS FAILURE" + "\n" + "\n" + bufFile.toString();
            }else{
                errorText = "RAID STATUS GOOD" + "\n" + "\n";
            }

        } catch (Exception e) {
            LogTool.getLogger().error("Error of read raidStatusFile: "+e.getMessage());
            errorText = "RAID STATUS: Error of read raidStatusFile: "+e.getMessage() + "\n" + "\n";
            hasTrouble = true;
        }
    }
}
