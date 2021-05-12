package ru.CustomOrthancWebMorda.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import ru.CustomOrthancWebMorda.beans.dicom.OrthancServer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

@ManagedBean(name = "mainBean", eager = true)
@SessionScoped
public class MainBean {

    public static OrthancServer mainServer;
    public String totalStudy = "0";
    public String totalPatient = "0";
    public String totalSize = "0";
    public DashboardModel model;
    public static String authentication;
    public String buffer;
    public String selectTheme;
    public ArrayList<String> themeList;

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
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        //DashboardColumn column2 = new DefaultDashboardColumn();
        //DashboardColumn column3 = new DefaultDashboardColumn();
        column1.addWidget("sports");
        model.addColumn(column1);
        //model.addColumn(column2);
        //model.addColumn(column3);

        System.out.println("init main");
        mainServer = new OrthancServer();
        mainServer.setIpaddress("192.168.1.58");//setIpaddress("185.59.139.156");//setIpaddress("192.168.0.6");//setIpaddress("185.59.139.156");
        mainServer.setPort("8042");//setPort("8142");
        mainServer.setLogin("doctor");
        mainServer.setPassword("doctor");
        //mainServer.setPathToJson("C:\\Program Files\\Orthanc Server\\Configuration\\");
         mainServer.setPathToJson("/etc/orthanc/");
        try {
            StringBuilder sb = makeGetConnectionAndStringBuilder("/statistics");
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
            System.out.println(e.getMessage().toString());
        }

        themeList = themeListinit();
        selectTheme = themeList.get(1);
    }

    public ArrayList<String> themeListinit(){
        ArrayList<String> themeList = new ArrayList<>();
        themeList.add("afterdark");
        themeList.add("afternoon");
        themeList.add("afterwork");
        themeList.add("black-tie");
        themeList.add("blitzer");
        themeList.add("bluesky");
        themeList.add("bootstrap");
        themeList.add("casablanca");
        themeList.add("cruze");
        themeList.add("cupertino");
        themeList.add("dark-hive");
        themeList.add("delta");
        themeList.add("dot-luv");
        themeList.add("eggplant");
        themeList.add("excite-bike");
        themeList.add("flick");
        themeList.add("glass-x");
        themeList.add("home");
        themeList.add("hot-sneaks");
        themeList.add("humanity");
        themeList.add("le-frog");
        themeList.add("midnight");
        themeList.add("mint-choc");
        themeList.add("overcast");
        themeList.add("pepper-grinder");
        themeList.add("redmond");
        themeList.add("rocket");
        themeList.add("sam");
        themeList.add("smoothness");
        themeList.add("south-street");
        themeList.add("start");
        themeList.add("sunny");
        themeList.add("swanky-purse");
        themeList.add("trontastic");
        themeList.add("ui-darkness");
        themeList.add("ui-lightness");
        themeList.add("vader");
        return themeList;
    }

    public StringBuilder makeGetConnectionAndStringBuilder(String apiUrl) {
        StringBuilder sb = null ;
        try {
            sb = new StringBuilder();
            HttpURLConnection conn = makeGetConnection(apiUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb;
    }

    private HttpURLConnection makeGetConnection(String apiUrl) throws Exception {
        HttpURLConnection conn  = null;
        String fulladdress = "http://"+ mainServer.getIpaddress()+":"+ mainServer.getPort();
        URL url = new URL(fulladdress+apiUrl);
        authentication = Base64.getEncoder().encodeToString((mainServer.getLogin()+":"+mainServer.getPassword()).getBytes());
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        if(authentication != null){
            conn.setRequestProperty("Authorization", "Basic " + authentication);
        }
        conn.getResponseMessage();
        return conn;
    }

    public DashboardModel getModel() {
        return model;
    }
}
