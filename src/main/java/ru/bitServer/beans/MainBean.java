package ru.bitServer.beans;

import org.primefaces.model.DashboardModel;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.DataAction;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.OrthancServer;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ManagedBean(name = "mainBean", eager = true)
@ApplicationScoped
public class MainBean implements UserDao, DataAction {

    public static OrthancServer mainServer;
    public static String pathToSaveResult;
    public static String osimisAddress;
    public DashboardModel model;
    public String selectTheme;
    public ArrayList<String> themeList;
    public String versionInfo;
    public static int timeOnWork;
    public static FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    public static FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    public static FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;
    public List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    public OrthancRestApi connection;
    List<BitServerStudy> allStudies = new ArrayList<>();
    public static Map<Long, Integer> resultMapLong = new TreeMap<>();
    public static Map<Long, Integer> resultMapShort = new TreeMap<>();

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

    @PostConstruct
    public void init() {
        versionInfo = "1.3";
        timeOnWork = 24;

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
            LogTool.getLogger().warn("Error init() MainBean: "+e.getMessage());
        }
        themeList = themeListinit();
        selectTheme = themeList.get(1);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new BaseUpdate(), 0, 1, TimeUnit.MINUTES);
    }

    public class BaseUpdate implements Runnable {

        @Override
        public void run() {
            OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
            syncDataBase(connection);
            allStudies = getAllBitServerStudy();
            allStudies.sort(Comparator.comparing(BitServerStudy::getSdate));
            resultMapLong.clear();
            resultMapShort.clear();
            resultMapLong = getStatMap(allStudies,"MM.yyyy");
            resultMapShort = getStatMap(allStudies,"yyyy");
        }
    }

    public static Map<Long, Integer> getResultMap(String pattern){
        Map<Long, Integer> resultMap = new TreeMap<>();
        switch (pattern){
            case("MM.yyyy"):
                resultMap = resultMapLong;
                break;
            case("yyyy"):
                resultMap = resultMapShort;
                break;
        }
        return resultMap;
    }

    public Map<Long, Integer> getStatMap(List<BitServerStudy> allStudies,String dateformat){
        Map<Long, Integer> resultMap = new TreeMap<>();
        DateFormat formatter = new SimpleDateFormat(dateformat);
        Date firstdate;
        Date seconddate;
        try {
            firstdate = allStudies.get(0).getSdate();
            seconddate = allStudies.get(allStudies.size() - 1).getSdate();
        }catch (Exception e){
            firstdate = new Date();
            seconddate = new Date();
        }
        for(BitServerStudy bufStudy:allStudies){
            if( (bufStudy.getSdate().after(firstdate)&&(bufStudy.getSdate().before(seconddate))) |
                    ( (bufStudy.getSdate().equals(firstdate))|(bufStudy.getSdate().equals(seconddate)) ) ){
                long bufDatemillis = 0;
                try {
                    bufDatemillis = (formatter.parse(formatter.format(bufStudy.getSdate()))).getTime();
                } catch (Exception e) {
                    LogTool.getLogger().warn("Error getStatMap: "+e.getMessage());
                }
                Integer bufCount = resultMap.get(bufDatemillis);
                if(bufCount==null){
                    resultMap.put(bufDatemillis,1);
                }else{
                    bufCount = bufCount + 1;
                    resultMap.replace(bufDatemillis,bufCount);
                }
            }
        }
        return resultMap;
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
