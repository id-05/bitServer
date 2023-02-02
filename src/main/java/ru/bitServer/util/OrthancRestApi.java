package ru.bitServer.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.bitServer.dicom.OrthancStudy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;

public class OrthancRestApi {

    private String authentication;
    private final String ipaddress;
    private final String port;
    private final String login;
    private final String password;
    final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

    public OrthancRestApi(String ipaddress, String port, String login, String password){
        this.ipaddress = ipaddress;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public StringBuilder makeGetConnectionAndStringBuilder(String apiUrl) {
        StringBuilder stringBuilder;
        try {
            stringBuilder = new StringBuilder();
            HttpURLConnection conn = makeGetConnection(apiUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                stringBuilder.append(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            LogTool.getLogger().error("Error makeGetConnectionAndStringBuilder restApi "+e.getMessage());
            stringBuilder = new StringBuilder();
            stringBuilder.append("error");
        }
        return stringBuilder;
    }

    public HttpURLConnection makeGetConnection(String apiUrl) throws Exception {
        HttpURLConnection conn;
        String fulladdress = "http://"+ ipaddress+":"+ port;
        URL url = new URL(fulladdress+apiUrl);
        authentication = Base64.getEncoder().encodeToString((login+":"+password).getBytes());
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        if(authentication != null){
            conn.setRequestProperty("Authorization", "Basic " + authentication);
        }
        conn.getResponseMessage();
        return conn;
    }

    public StringBuilder makePostConnectionAndStringBuilder(String apiUrl, String post)  {
        StringBuilder sb;
        HttpURLConnection conn;
        try {
            sb = new StringBuilder();
            conn = makePostConnection(apiUrl, post);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
            conn.getResponseMessage();
        } catch (Exception e) {
            LogTool.getLogger().error("Error makePostConnectionAndStringBuilder "+e.getMessage());
            return null;
        }
        return sb;
    }

    public StringBuilder makePostConnectionAndStringBuilderWithIOE(String apiUrl, String post) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            HttpURLConnection conn = makePostConnection(apiUrl, post);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            //conn.disconnect();
            conn.getResponseMessage();
        } catch (Exception e) {
            LogTool.getLogger().error("Error makePostConnectionAndStringBuilderWithIOE "+e.getMessage());
            return null;
        }
        return sb;
    }

    public HttpURLConnection makePostConnection(String apiUrl, String post) throws Exception {
        String fulladdress = "http://" + ipaddress + ":" + port;
        HttpURLConnection conn;
        URL url = new URL(fulladdress + apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        authentication = Base64.getEncoder().encodeToString((login + ":" + password).getBytes());
        if (authentication != null) {
            conn.setRequestProperty("Authorization", "Basic " + authentication);
        }
        OutputStream os = conn.getOutputStream();
        os.write(post.getBytes());
        os.flush();
        return conn;
    }

    public void deleteStudyFromOrthanc(String studyId) throws IOException {
        String fulladdress = "http://" + ipaddress + ":" + port;
        URL url = new URL(fulladdress + "/studies/" + studyId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("DELETE");
        authentication = Base64.getEncoder().encodeToString((login + ":" + password).getBytes());
        if(this.authentication != null){
            conn.setRequestProperty("Authorization", "Basic " + this.authentication);
        }
        conn.getResponseMessage();
        conn.disconnect();
    }

    public StringBuilder getStatistics(){
        return makeGetConnectionAndStringBuilder("/statistics");
    }

    public String sendDicom(String apiUrl, byte[] post) {
        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();
        String resultID ="";
        try {
            String fulladdress = "http://" + ipaddress + ":" + port;
            URL url=new URL(fulladdress+apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            authentication = Base64.getEncoder().encodeToString((login+":"+password).getBytes());
            if(this.authentication != null){
                conn.setRequestProperty("Authorization", "Basic " + this.authentication);
            }
            conn.setRequestProperty("content-length", Integer.toString(post.length));
            conn.setRequestProperty("content-type", "application/dicom");
            OutputStream os = conn.getOutputStream();
            os.write(post);

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            JsonParser parser = new JsonParser();
            JsonObject orthancJson=new JsonObject();
            try {
                orthancJson = parser.parse(sb.toString()).getAsJsonObject();
            }catch (Exception e){
                LogTool.getLogger().error("Error parse json JsonSetings: "+e.getMessage());
            }

            if (orthancJson.has("ParentStudy")) resultID = orthancJson.get("ParentStudy").getAsString();
            os.flush();

        } catch (Exception e) {
            LogTool.getLogger().error("Error sendDicom RestApi "+e.getMessage());
        }
        return resultID;
    }

    public OrthancStudy parseStudy(JsonObject studyData){
        JsonObject parentPatientDetails = null;
        if (studyData.has("PatientMainDicomTags")) {
            parentPatientDetails = studyData.get("PatientMainDicomTags").getAsJsonObject();
        }
        String parentPatientID = studyData.get("ParentPatient").getAsString();
        String studyId = studyData.get("ID").getAsString();
        JsonObject studyDetails = studyData.get("MainDicomTags").getAsJsonObject();
        String patientSex = "N/A";
        String patientName = "N/A";
        String patientId = "N/A";
        String patientDobString = "N/A";
        Date patientDob = null;

        assert parentPatientDetails != null;
        if (parentPatientDetails.has("PatientBirthDate")) {
            patientDobString = parentPatientDetails.get("PatientBirthDate").getAsString();
        }

        if(!patientDobString.equals("")){
            try {
                patientDob = FORMAT.parse(patientDobString);
            } catch (Exception e) {
                LogTool.getLogger().debug("Error to transfer date 1 QueueBean "+e.getMessage());
            }
        }

        if (parentPatientDetails.has("PatientSex")) {
            patientSex = parentPatientDetails.get("PatientSex").getAsString();
        }

        if (parentPatientDetails.has("PatientName")) {
            patientName = parentPatientDetails.get("PatientName").getAsString();
        }

        if (parentPatientDetails.has("PatientID")) {
            patientId = parentPatientDetails.get("PatientID").getAsString();
        }

        String accessionNumber = "N/A";
        if (studyDetails.has("AccessionNumber")) {
            accessionNumber = studyDetails.get("AccessionNumber").getAsString();
        }
        String studyInstanceUid = studyDetails.get("StudyInstanceUID").getAsString();
        String studyDate = null;
        Date studyDateObject = new Date();
        if (studyDetails.has("StudyDate")) {
            studyDate = studyDetails.get("StudyDate").getAsString();
        }

        try {
            studyDateObject = FORMAT.parse(studyDate);
        } catch (Exception e) {
            //LogTool.getLogger().error("Error transfer getStudiesFromJson() QueueBean "+e.getMessage());
        }

        String studyDescription = "N/A";
        if (studyDetails.has("StudyDescription")) {
            studyDescription = studyDetails.get("StudyDescription").getAsString();
        }

        String studyInstitutionName = "N/A";
        if (studyDetails.has("InstitutionName")) {
            studyInstitutionName = studyDetails.get("InstitutionName").getAsString();
        }

        String studyModality = "N/A";
        if (studyData.has("Series")) {
            JsonArray SeriesArray = studyData.get("Series").getAsJsonArray();
            String bufSerie = SeriesArray.get(0).getAsString();
            StringBuilder sb = makeGetConnectionAndStringBuilder("/series/"+bufSerie);
            JsonParser parserJsonSerie = new JsonParser();
            JsonObject serie = (JsonObject) parserJsonSerie.parse(sb.toString());
            JsonObject serieMainDicomTags = null;
            if (serie.has("MainDicomTags")) {
                serieMainDicomTags = serie.get("MainDicomTags").getAsJsonObject();
            }
            assert serieMainDicomTags != null;
            if (serieMainDicomTags.has("Modality")) {
                studyModality = serieMainDicomTags.get("Modality").getAsString();
            }
        }
        if (!patientName.equals("ANONIM")) {
            return new OrthancStudy(studyInstitutionName, studyDescription, studyModality,
                    studyDateObject, accessionNumber, studyId, patientName, patientId, patientDob, patientSex, parentPatientID, studyInstanceUid);
        }else{
            return new OrthancStudy("ANONIM");
        }
    }

    public ArrayList<OrthancStudy> getStudiesFromJson(String data) {
        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(data);
        Iterator<JsonElement> studiesIterator = studies.iterator();
        ArrayList<OrthancStudy> studyList = new ArrayList<>();

        while (studiesIterator.hasNext()) {
            JsonObject studyData = (JsonObject) studiesIterator.next();
            OrthancStudy studyObj = parseStudy(studyData);
            studyList.add(studyObj);
        }
        return studyList;
    }



}
