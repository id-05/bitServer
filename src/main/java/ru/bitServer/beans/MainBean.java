package ru.bitServer.beans;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.protocol.impl.AppRoutingDataImpl;
import org.primefaces.model.DashboardModel;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.DataAction;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.OrthancServer;
import ru.bitServer.service.HL7toWorkList;
import ru.bitServer.util.DeleteWorkListFile;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@ManagedBean(name = "mainBean", eager = true)
@ApplicationScoped
public class MainBean implements UserDao, DataAction {

    public static OrthancServer mainServer;
    public static String pathToSaveResult;
    public static String osimisAddress;
    public String periodUpdate = "2";
    private static HL7Service ourHl7Server;
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
    public static List<BitServerStudy> allStudies = new ArrayList<>();
    public static Map<Long, Integer> resultMapLong = new TreeMap<>();
    public static Map<Long, Integer> resultMapShort = new TreeMap<>();
    static HapiContext context = new DefaultHapiContext();
    public static final String url = "jdbc:mysql://192.168.1.58:3306/orthanc";
    public static final String user = "orthanc";
    public static final String password = "orthanc";
    boolean showStat;

    public static final String url2 = "jdbc:postgresql://192.168.1.58:5432/orthanc";


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
        versionInfo = "1.4";
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
                    case "addressforosimis":
                        osimisAddress = buf.getRvalue();
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
            LogTool.setFileName(getBitServerResource("logpath").getRvalue());
        }catch (Exception e){
            saveBitServiceResource(new BitServerResources("logpath","/dataimage/results/"));
            LogTool.setFileName(getBitServerResource("logpath").getRvalue());
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        if(!periodUpdate.equals("0")) {
            scheduler.scheduleAtFixedRate(new BaseUpdate(), 0, Long.valueOf(periodUpdate), TimeUnit.MINUTES);
        }
            ScheduledExecutorService scheduler2 = Executors.newSingleThreadScheduledExecutor();
        scheduler2.scheduleAtFixedRate(new DeleteWorkListFile(), 0, 5, TimeUnit.MINUTES);

        HL7service();

    }

    public static void HL7service() {
        boolean useSecureConnection = false;
        BitServerResources bufRes = UserDao.getStaticBitServerResource("hl7port");
        int port = 4243;
        if(bufRes!=null){
            port = Integer.parseInt(UserDao.getStaticBitServerResource("hl7port").getRvalue());
        }
        ourHl7Server = context.newServer(port, useSecureConnection);
        AppRoutingDataImpl ourRouter = new AppRoutingDataImpl("*", "*", "*", "*");
        ourHl7Server.registerApplication(ourRouter, new HL7toWorkList());
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

    public class BaseUpdate implements Runnable {
        @Override
        public void run() {
            if(getBitServerResource("debug").getRvalue().equals("true")) {
                LogTool.getLogger().info("start update");
            }

            OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
            try {
                syncDataBase(connection);
            } catch (Exception e) {
                LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
            }

            if(showStat){
                double startTime = new Date().getTime();
                System.out.println("calculate statistics");
                allStudies = getAllBitServerStudyJDBC();
                allStudies.sort(Comparator.comparing(BitServerStudy::getSdate));
                resultMapLong.clear();
                resultMapShort.clear();
                resultMapLong = getStatMap(allStudies,"MM.yyyy");
                resultMapShort = getStatMap(allStudies,"yyyy");
                double calculateStateTime = ((new Date().getTime())-startTime)/1000;
                if(getBitServerResource("debug").getRvalue().equals("true")) {
                    LogTool.getLogger().info("calculateStateTime = "+calculateStateTime+"c.");
                }
            }

//            Properties props = new Properties();
//            props.setProperty("user", "orthanc");
//            props.setProperty("password", "orthanc");
//            try {
//                Class.forName("org.postgresql.Driver");
//                java.sql.Connection conn =  DriverManager.getConnection(url2, props);
//
//                String selectTableSQL = "SELECT DISTINCT patientid, part1.publicid, tag1.value, tag2.value," +
//                        "tag3.value, tag4.value, tag5.value, tag6.value, tag7.value, tag8.value FROM patientrecyclingorder" +
//                        " INNER JOIN resources AS part1 ON part1.parentid = patientrecyclingorder.patientid" +
//                        " INNER JOIN resources AS part2 ON part2.parentid = part1.internalid"+
//                        " INNER JOIN maindicomtags AS tag1 ON tag1.id = part1.internalid AND tag1.taggroup = '16' AND tag1.tagelement = '16'"+
//                        " INNER JOIN maindicomtags AS tag2 ON tag2.id = part1.internalid AND tag2.taggroup = '16' AND tag2.tagelement = '32'"+
//                        " INNER JOIN maindicomtags AS tag3 ON tag3.id = part1.internalid AND tag3.taggroup = '16' AND tag3.tagelement = '48'"+
//                        " INNER JOIN maindicomtags AS tag4 ON tag4.id = part1.internalid AND tag4.taggroup = '16' AND tag4.tagelement = '64'"+
//                        " INNER JOIN maindicomtags AS tag5 ON tag5.id = part1.internalid AND tag5.taggroup = '8' AND tag5.tagelement = '32'"+
//                        " INNER JOIN maindicomtags AS tag6 ON tag6.id = part1.internalid AND tag6.taggroup = '8' AND tag6.tagelement = '128'"+
//                        " INNER JOIN maindicomtags AS tag7 ON tag7.id = part1.internalid AND tag7.taggroup = '8' AND tag7.tagelement = '4144'"+
//                        " INNER JOIN maindicomtags AS tag8 ON tag8.id = part2.internalid AND tag8.taggroup = '8' AND tag8.tagelement = '96'";
//                Statement statement = conn.createStatement();
//                ResultSet rs = statement.executeQuery(selectTableSQL);
//                while (rs.next()) {
//                   System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5)+" "+
//                           rs.getString(6)+" "+rs.getString(7)+" "+rs.getString(8)+" "+rs.getString(9)+" "+rs.getString(10));
//                }
//                conn.close();
//            } catch (SQLException | ClassNotFoundException throwables) {
//                throwables.printStackTrace();
//            }
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
                    LogTool.getLogger().error(this.getClass().getSimpleName()+": Error getStatMap: "+e.getMessage());
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
