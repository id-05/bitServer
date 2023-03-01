package ru.bitServer.beans;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.protocol.impl.AppRoutingDataImpl;
import org.primefaces.model.DashboardModel;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.DataAction;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.OrthancServer;
import ru.bitServer.service.HL7toWorkList;
import ru.bitServer.util.DeleteWorkListFile;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.TimersLauncher;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ManagedBean(name = "mainBean", eager = true)
@ApplicationScoped
public class MainBean implements UserDao, DataAction {

    public static OrthancServer mainServer;
    public static String pathToSaveResult;
    public String periodUpdate = "2";
    public DashboardModel model;
    public String selectTheme;
    public ArrayList<String> themeList;
    public String versionInfo;
    public static int timeOnWork;
    static boolean hasTrouble;
    public static FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    public static FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    public static FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;
    public List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    public OrthancRestApi connection;
    static HapiContext context = new DefaultHapiContext();

    public static final String user = "orthanc";
    public static final String password = "orthanc";
    //public static final String url = "jdbc:postgresql://192.168.1.58:5432/orthanc";
    public static final String url = "jdbc:postgresql://127.0.0.1:5432/orthanc";

    boolean showStat;
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

    public ArrayList<String> getThemeList() {
        return themeList;
    }

    @PostConstruct
    public void init() {
        versionInfo = "2.2";
        hasTrouble = false;
        timeOnWork = 24;
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
                    case "PeriodUpdate": periodUpdate = buf.getRvalue();
                        break;
                    case "showStat": showStat = buf.getRvalue().equals("true");
                        break;
                }
            }
        }catch (Exception e){
            LogTool.getLogger().error(Class.class.getCanonicalName() +": Error init() MainBean: "+e.getMessage());
        }
        themeList = themeListinit();
        selectTheme = themeList.get(1);

        try {
            periodUpdate = getBitServerResource("PeriodUpdate").getRvalue();
        }catch (Exception e){
            saveBitServiceResource(new BitServerResources("PeriodUpdate","2"));
        }

        try {
            if(getBitServerResource("deleteBufFileAfter").getRvalue().equals("")){
                saveBitServiceResource(new BitServerResources("deleteBufFileAfter","false"));
            }
        }catch (Exception e){
            saveBitServiceResource(new BitServerResources("deleteBufFileAfter","false"));
        }

        try {
            if(getBitServerResource("worklistsamplefile").getRvalue().equals("")){
                saveBitServiceResource(new BitServerResources("worklistsamplefile","/bitserver/samplewl.txt"));
            }

        }catch (Exception e){
            saveBitServiceResource(new BitServerResources("worklistsamplefile","/bitserver/samplewl.txt"));
        }

        try {
            LogTool.setFileName(getBitServerResource("logpath").getRvalue());
        }catch (Exception e){
            saveBitServiceResource(new BitServerResources("logpath","/dataimage/results/"));
            LogTool.setFileName(getBitServerResource("logpath").getRvalue());
        }


//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //ЗДЕСЬ СОБИРАТЬ СТАТИСТИКУ

        //удаление файлов рабочих списков
        try {
            int wlDelHourCount = Integer.parseInt(getBitServerResource("workListLifeTime").getRvalue());
            if(wlDelHourCount!=0){
                ScheduledExecutorService delWL = Executors.newSingleThreadScheduledExecutor();
                delWL.scheduleAtFixedRate(new DeleteWorkListFile(), 0, 5, TimeUnit.MINUTES);
            }
        }catch (Exception e){
            LogTool.getLogger().error("Error during parce Integer.parseInt(getBitServerResource(\"workListLifeTime\").getRvalue()");
        }


        //Обработка таймеров
        if(getBitServerResource("timerEnable").getRvalue().equals("true")){
            ScheduledExecutorService timeExecServ = Executors.newSingleThreadScheduledExecutor();
            timeExecServ.scheduleAtFixedRate(new TimersLauncher(), 0, 50, TimeUnit.SECONDS);
        }

       HL7service();
    }

    public static void HL7service() {
        BitServerResources bufRes = UserDao.getStaticBitServerResource("hl7port");
        int port = 104;
        try {
            port = Integer.parseInt(bufRes.getRvalue());
        }catch (Exception e){
            LogTool.getLogger().error("Error UserDao.getStaticBitServerResource(\"hl7port\")");
        }

        LogTool.getLogger().info("hl7port: "+UserDao.getStaticBitServerResource("hl7port").getRvalue());
        LogTool.getLogger().info("WorkListPath: "+UserDao.getStaticBitServerResource("WorkListPath").getRvalue());
        HL7Service ourHl7Server = context.newServer(port, false);
        AppRoutingDataImpl ourRouter = new AppRoutingDataImpl("*", "*", "*", "*");
        ourHl7Server.registerConnectionListener(new OurConnectionListener());
        ourHl7Server.registerApplication(ourRouter, new HL7toWorkList());
        try {
            ourHl7Server.startAndWait();
        }catch (Exception e){
            LogTool.getLogger().error("MainBean Error HL7service: "+e.getMessage());
        }
    }

    static class OurConnectionListener implements ConnectionListener {

        @Override
        public void connectionDiscarded(Connection connectionBeingDiscarded) {
            System.out.println("Connection discarded event fired " + connectionBeingDiscarded.getRemoteAddress());
            System.out.println("For Remote Address: " + connectionBeingDiscarded.getRemoteAddress());
            System.out.println("For Remote Port: " + connectionBeingDiscarded.getRemotePort());
        }

        @Override
        public void connectionReceived(Connection connectionBeingOpened) {
            System.out.println("Connection opened event fired " + connectionBeingOpened.getRemoteAddress());
            System.out.println("From Remote Address: " + connectionBeingOpened.getRemoteAddress());
            System.out.println("From Remote Port: " + connectionBeingOpened.getRemotePort());
        }
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
