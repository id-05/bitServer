package ru.bitServer.beans;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.OrthancJob;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "jobsBean", eager = true)
@ViewScoped
public class JobsBean implements UserDao {


    List<OrthancJob> orthancJobs = new ArrayList<>();

    List<OrthancJob> selectedOrthancJobs = new ArrayList<>();

    OrthancJob selectedOrthancJob = new OrthancJob();

    OrthancRestApi connection;
    StringBuilder stringBuilder;

    public OrthancJob getSelectedOrthancJob() {
        return selectedOrthancJob;
    }

    public void setSelectedOrthancJob(OrthancJob selectedOrthancJob) {
        this.selectedOrthancJob = selectedOrthancJob;
    }

    public List<OrthancJob> getOrthancJobs() {
        return orthancJobs;
    }

    public void setOrthancJobs(List<OrthancJob> orthancJobs) {
        this.orthancJobs = orthancJobs;
    }

    public List<OrthancJob> getSelectedOrthancJobs() {
        return selectedOrthancJobs;
    }

    public void setSelectedOrthancJobs(List<OrthancJob> selectedOrthancJobs) {
        this.selectedOrthancJobs = selectedOrthancJobs;
    }

    @PostConstruct
    public void init() {
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        orthancJobs = getAllJobs();
    }

    public void resumeAllPause(){
        System.out.println("resume all pause");
    }

    public void pauseAllRunning(){
        System.out.println("pause all");
    }

    public void cancelAllRunning(){
        System.out.println("cancelAllRunning");
    }



    public List<OrthancJob> getAllJobs(){
        List<OrthancJob> allJobs = new ArrayList<>();
        stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs");
        String[] jobs = stringBuilder.toString().replace("[","").replace("]","").split(",");
        for(String job:jobs) {
            stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs/"+job.replace(" ","").replace("\"",""));
            OrthancJob orJob = new OrthancJob(stringBuilder.toString());
            System.out.println(orJob.getIdentificator());
            allJobs.add(orJob);
        }
        return allJobs;
    }

    public void getInfo(){
        System.out.println("getInfo()");
    }

    public void pause(String buf){
        stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs/"+buf+"/pause");
        LogTool.getLogger().info("pause answer api: "+stringBuilder.toString());
    }

    public void resume(String buf){
        stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs/"+buf+"/resume");
        LogTool.getLogger().info("resume answer api: "+stringBuilder.toString());
    }

    public void resubmit(String buf){
        stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs/"+buf+"/resubmit");
        LogTool.getLogger().info("resubmit answer api: "+stringBuilder.toString());
    }

    public void cancel(String buf){
        stringBuilder = connection.makeGetConnectionAndStringBuilder("/jobs/"+buf+"/cancel");
        LogTool.getLogger().info("cancel answer api: "+stringBuilder.toString());
    }

    public void DelJob(){
        System.out.println("DelJob()");
    }

}
