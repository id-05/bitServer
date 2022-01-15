package ru.bitServer.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.OrthancServer;
import ru.bitServer.util.OrthancRestApi;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "mainBean", eager = true)
@SessionScoped
public class MainBean implements UserDao {

    public static OrthancServer mainServer;
    public static String pathToSaveResult;
    public static String osimisAddress;
    public String totalStudy = "0";
    public String totalPatient = "0";
    public String totalSize = "0";
    public DashboardModel model;
    public String buffer;
    public String selectTheme;
    public ArrayList<String> themeList;
    public String versionInfo;
    public static int timeOnWork;
    public static FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    public static FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    public static FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;
    public List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    public OrthancRestApi connection;

    public int getTimeOnWork() {
        return timeOnWork;
    }

    public void setTimeOnWork(int timeOnWork) {
        MainBean.timeOnWork = timeOnWork;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getSelectTheme() {
        return selectTheme;
    }

    public void setSelectTheme(String selectTheme) {
        this.selectTheme = selectTheme;
    }

    public ArrayList<String> getThemeList() {
        return themeList;
    }

    public void setThemeList(ArrayList<String> themeList) {
        this.themeList = themeList;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public String getTotalStudy() {
        return totalStudy;
    }

    public void setTotalStudy(String totalStudy) {
        this.totalStudy = totalStudy;
    }

    public String getTotalPatient() {
        return totalPatient;
    }

    public void setTotalPatient(String totalPatient) {
        this.totalPatient = totalPatient;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    @PostConstruct
    public void init() {
        versionInfo = "1.1";
        timeOnWork = 24;

        //model = new DefaultDashboardModel();
        //DashboardColumn column1 = new DefaultDashboardColumn();
        //DashboardColumn column2 = new DefaultDashboardColumn();
        //DashboardColumn column3 = new DefaultDashboardColumn();
        //column1.addWidget("sports");
        //model.addColumn(column1);
        //model.addColumn(column2);
        //model.addColumn(column3);

        System.out.println("init main");
        mainServer = new OrthancServer();
        try {
            bitServerResourcesList = getAllBitServerResource();
            for (BitServerResources buf : bitServerResourcesList) {
                switch (buf.getRname()) {
                    case "httpmode":
                        mainServer.setHttpmode(buf.getRvalue());
                        break;
                    case "orthancaddress":
                        mainServer.setIpaddress(buf.getRvalue());
                        break;
                    case "port":
                        mainServer.setPort(buf.getRvalue());
                        break;
                    case "login":
                        mainServer.setLogin(buf.getRvalue());
                        break;
                    case "password":
                        mainServer.setPassword(buf.getRvalue());
                        break;
                    case "pathtojson":
                        mainServer.setPathToJson(buf.getRvalue());
                        break;
                    case "pathtoresultfile":
                        pathToSaveResult = buf.getRvalue();
                        break;
                    case "addressforosimis":
                        osimisAddress = buf.getRvalue();
                        break;
                }
            }
        }catch (Exception e){
            System.out.println("Ошибка: "+e.getMessage());
        }
        try {
            connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
            System.out.println("connection : "+mainServer.getIpaddress()+":"+mainServer.getPort());
            StringBuilder sb = connection.makeGetConnectionAndStringBuilder("/statistics");
            JsonParser parser = new JsonParser();
            JsonObject orthancJson = parser.parse(sb.toString()).getAsJsonObject();
            mainServer.setCountInstances(orthancJson.get("CountInstances").getAsInt());
            mainServer.setCountPatients(orthancJson.get("CountPatients").getAsInt());
            mainServer.setCountSeries(orthancJson.get("CountSeries").getAsInt());
            mainServer.setCountStudies(orthancJson.get("CountStudies").getAsInt());
            mainServer.setTotalDiskSizeMB(orthancJson.get("TotalDiskSizeMB").getAsInt());
            totalStudy = String.valueOf(mainServer.getCountStudies());
            totalPatient = String.valueOf(mainServer.getCountPatients());
            totalSize = String.valueOf(mainServer.getTotalDiskSizeMB()/1024);
        }catch (Exception e){
            System.out.println("Ошибка: "+e.getMessage());
        }
        themeList = themeListinit();
        selectTheme = themeList.get(1);
    }

    public ArrayList<String> themeListinit(){
        ArrayList<String> themeList = new ArrayList<>();
        themeList.add("nova-light");
        themeList.add("nova-dark");
        themeList.add("nova-colored");
        themeList.add("luna-blue");
        themeList.add("luna-amber");
        themeList.add("luna-green");
        themeList.add("luna-pink");
        return themeList;
    }

    public DashboardModel getModel() {
        return model;
    }
}
