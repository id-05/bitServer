package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.Logfile;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean(name = "logOrthancBean", eager = true)
@ViewScoped
public class LogOrthancBean implements UserDao {
    List<Logfile> logFileList = new ArrayList<>();
    Logfile selectedFile;
    String logpath = null;

    public List<Logfile> getLogFileList() {
        return logFileList;
    }

    public void setLogFileList(List<Logfile> logFileList) {
        this.logFileList = logFileList;
    }

    public Logfile getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(Logfile selectedFile) {
        this.selectedFile = selectedFile;
    }

    @PostConstruct
    public void init() {
        logpath = getBitServerResource("logpathorthanc").getRvalue();
        if (logpath.equals("empty")){
            logpath = "/var/log/orthanc";
        }
        selectedFile = new Logfile();
        logFileList = getLogFiles();
    }

    public List<Logfile> getLogFiles(){
        List<Logfile> resultlist = new ArrayList<>();
        File dir = new File(logpath);
        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);
        for(File bufFile:lst){
            resultlist.add(new Logfile(bufFile.getName(),bufFile));
        }
        return resultlist;
    }

    public void deleteFile(){
        File buf = selectedFile.getFile();
        if(buf.delete()){
            logFileList.remove(selectedFile);
            selectedFile = new Logfile();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Файл удален!"));
            PrimeFaces.current().ajax().update(":form:dt-logfiles:");
        }
    }

    public StreamedContent getGetResult() throws IOException {
        String strpath = logpath+"/"+selectedFile.getRname();
        InputStream inputStream = new FileInputStream(strpath);
        return DefaultStreamedContent.builder()
                .name(selectedFile.getRname())
                .contentType("application/txt")
                .stream(() -> inputStream)
                .build();
    }

    public void deleteAllLog(){
        for(Logfile buflog:logFileList){
            buflog.getFile().delete();
        }
        logFileList.clear();
        selectedFile = new Logfile();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Файл удален!"));
        PrimeFaces.current().ajax().update(":form:dt-logfiles:");
    }
}

