package ru.bitServer.beans;

import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.OrthancJob;
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
    Integer numTask;

    String countTask;
    boolean existTask = false;
    boolean debug;

    public String getCountTask() {
        return countTask;
    }

    public void setCountTask(String countTask) {
        this.countTask = countTask;
    }

    public boolean isExistTask() {
        return existTask;
    }

    public void setExistTask(boolean existTask) {
        this.existTask = existTask;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getErrorText() {
        return errorText;
    }

    public boolean isHasTrouble() {
        return hasTrouble;
    }

    OrthancRestApi connection;
    StringBuilder stringBuilder;


    @PostConstruct
    public void init(){
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        debug = getBitServerResource("debug").getRvalue().equals("true");
        numTask = getCountAllTask();
        countTask = "Активных заданий: "+numTask;
        if(numTask>0){
            existTask = true;
        }
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

    public Integer getCountAllTask(){
        Integer result = 0;
        stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs");
        try {
            if (!stringBuilder.toString().equals("[]") & !stringBuilder.toString().equals("error")) {
                String[] jobs = stringBuilder.toString().replace("[", "").replace("]", "").split(",");
                for (String job : jobs) {
                    stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs/" + job.replace(" ", "").replace("\"", ""));
                    OrthancJob orJob = new OrthancJob(stringBuilder.toString());
                    if (orJob.getState().equals("Running")) result++;
                }
            }
        } catch(Exception e){
            LogTool.getLogger().error("getjobs: "+e.getMessage()+" / "+stringBuilder.toString());
        }
        return result;
    }

    public void orthancGetInfo(){
        try{
            stringBuilder = connection.makeGetConnectionAndStringBuilder("/statistics");
            if(!stringBuilder.toString().contains("error")){
                errorText = errorText + "\n"+ "\n" + "\n"+"ORTHANC STATUS GOOD" + "\n" + "\n";
            }else{
                errorText = errorText + "\n"+ "\n" + "\n"+"ORTHANC STATUS FAILURE" + "\n" + "\n";
                hasTrouble = true;
            }
        }catch (Exception e){
            LogTool.getLogger().error(LogTool.getLogger() + " Error of orthancGetInfo: "+e.getMessage());
            hasTrouble = true;
        }
    }

    public void raidGetInfo(){
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/raidinfo");
            proc.waitFor();
        }catch (Exception e){
            LogTool.getLogger().error(LogTool.getLogger() +" Error during raidGetInfo()");
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
            LogTool.getLogger().error(LogTool.getLogger() +" Error of read raidStatusFile: "+e.getMessage());
            errorText = "RAID STATUS: Error of read raidStatusFile: "+e.getMessage() + "\n" + "\n";
            hasTrouble = true;
        }
    }
}
