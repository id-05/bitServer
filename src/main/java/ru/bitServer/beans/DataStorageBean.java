package ru.bitServer.beans;

import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.File;

@ManagedBean(name = "dataStorageBean")
@ViewScoped
public class DataStorageBean implements UserDao {

    String usedSpace = "?";
    String totalSpace = "?";
    String freeSpace = "?";
    String directory = "?";

    public String getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(String usedSpace) {
        this.usedSpace = usedSpace;
    }

    public String getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(String totalSpace) {
        this.totalSpace = totalSpace;
    }

    public String getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(String freeSpace) {
        this.freeSpace = freeSpace;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @PostConstruct
    public void init() {
        try {
            BitServerResources dataStorageResource = getBitServerResource("datastorage");
            if(dataStorageResource!=null) {
                String bufDataStorage = dataStorageResource.getRvalue();
                directory = bufDataStorage;
                File bufFile = new File(bufDataStorage);
                totalSpace = (int) (bufFile.getTotalSpace() / (double) (1024 * 1024 * 1024)) + " Gb";
                freeSpace = (int) (bufFile.getFreeSpace() / (double) (1024 * 1024 * 1024)) + " Gb";
                double bufDouble = (bufFile.getFreeSpace() / (double) bufFile.getTotalSpace());
                usedSpace = (100 - (int) (bufDouble * 100)) + " %";
            }
        }catch (Exception e){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            LogTool.getLogger().warn("Error in DataStorageBean: "+e.getMessage());
//            try{
//                ec.redirect(ec.getRequestContextPath()
//                        + "/views/errorpage.xhtml"+"?"+e.getMessage());
//            }catch (Exception e2){
//                LogTool.getLogger().warn("Error during redirect from DataStorageBean: "+e2.getMessage());
//            }
        }
    }
}
