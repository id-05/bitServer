package ru.CustomOrthancWebMorda.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@ManagedBean(name = "mainBean", eager = false)
@ViewScoped
public class MainBean {

    public static OrthancServer mainServer;
    public String totalStudy;
    public String totalPatient;
    public String totalSize;
    private DashboardModel model;
    public static String authentication;
    public String buffer;

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
        buffer ="test";

        System.out.println("init main");
        mainServer = new OrthancServer();
        mainServer.setIpaddress("192.168.0.6");//setIpaddress("185.59.139.156");
        mainServer.setPort("8042");//setPort("8142");
        mainServer.setLogin("doctor");
        mainServer.setPassword("doctor");
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

            model = new DefaultDashboardModel();
            DashboardColumn column1 = new DefaultDashboardColumn();
            DashboardColumn column2 = new DefaultDashboardColumn();
            DashboardColumn column3 = new DefaultDashboardColumn();

            column1.addWidget("sports");

            model.addColumn(column1);
            model.addColumn(column2);
            model.addColumn(column3);
        }catch (Exception e){
            System.out.println(e.getMessage().toString());
        }

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
        HttpURLConnection conn=null;
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
