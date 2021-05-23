package ru.bitServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import ru.bitServer.dicom.Patient;
import ru.bitServer.dicom.Serie;
import ru.bitServer.dicom.Study;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "serieBean", eager = true)
@SessionScoped
public class SerieBean {

    public String serieID;
    public static String patientId;
    public static Patient currentPatient;
    public static String authentication;
    public Study currentStudy;
    public ArrayList<Serie> serieList = new ArrayList<>();
    public Serie bufSerie = new Serie();

    public Serie getBufSerie() {
        return bufSerie;
    }

    public void setBufSerie(Serie bufSerie) {
        this.bufSerie = bufSerie;
    }


    public ArrayList<Serie> getSerieList() {
        return serieList;
    }

    public void setSerieList(ArrayList<Serie> serieList) {
        this.serieList = serieList;
    }

    public Study getCurrentStudy() {
        return currentStudy;
    }

    public void setCurrentStudy(Study currentStudy) {
        this.currentStudy = currentStudy;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        SerieBean.currentPatient = currentPatient;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        SerieBean.patientId = patientId;
    }

    public String getStudyID() {
        return studyID;
    }

    public void setStudyID(String studyID) {
        this.studyID = studyID;
    }

    public String studyID;

    public String getSerieID() {
        return serieID;
    }

    public void setSerieID(String serieID) {
        this.serieID = serieID;
    }

    @PostConstruct
    public void init(){
        System.out.println("serieBean");
        patientId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("patientID").toString();
        studyID = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("studyID").toString();

        for(Patient bufPatient:SearchBean.patients){
            if(bufPatient.getPatientOrthancId().equals(patientId)){
                currentPatient = bufPatient;
            }
        }

        for(Study bufStudy:PatientInfoBean.studyList){
            if(bufStudy.getOrthancId().equals(studyID)){
                currentStudy = bufStudy;
            }
        }

        StringBuilder sb = makeGetConnectionAndStringBuilder("/studies/" +currentStudy.getOrthancId()+"/series");
        serieList = getSeriesFromJson(sb.toString());
    }

    public void redirectToOsimis(SelectEvent<Serie> event) {
        String buf = event.getObject().getId();
        System.out.println("series id for redirect "+buf);
        PrimeFaces.current().executeScript("window.open('http://192.168.1.58:8042/osimis-viewer/app/index.html?series="+buf+"','_blank')");
    }

    public ArrayList<Serie> getSeriesFromJson(String data){
        JsonParser parserJson = new JsonParser();
        ArrayList<Serie> bufSeries = new ArrayList<>();

        JsonArray seriesArray = (JsonArray) parserJson.parse(data);

        for (JsonElement jsonElement : seriesArray) {
            JsonObject serieData = (JsonObject) jsonElement;
            JsonObject mainDicomTags = serieData.get("MainDicomTags").getAsJsonObject();
            String serieId = serieData.get("ID").getAsString();
            String seriesDescription = "N/A";
            if (mainDicomTags.has("SeriesDescription")) {
                seriesDescription = mainDicomTags.get("SeriesDescription").getAsString();
            }
            String seriesNumber = "N/A";
            if (mainDicomTags.has("SeriesNumber")) {
                seriesNumber = mainDicomTags.get("SeriesNumber").getAsString();
            }
            JsonArray instances = serieData.get("Instances").getAsJsonArray();
            Serie newSerie = new Serie(seriesDescription, seriesNumber, instances, instances.size(), serieId);
            bufSeries.add(newSerie);
        }
        return bufSeries;
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

    public void backpressed() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("patientID", patientId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("patientinfo.xhtml");
    }
}
