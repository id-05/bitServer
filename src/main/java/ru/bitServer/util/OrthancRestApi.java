package ru.bitServer.util;

import ru.bitServer.dao.BitServerStudy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class OrthancRestApi {

    private String authentication;
    private final String ipaddress;
    private final String port;
    private final String login;
    private final String password;

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
            e.printStackTrace();
            return null;
        }
        return stringBuilder;
    }

    private HttpURLConnection makeGetConnection(String apiUrl) throws Exception {
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

    public StringBuilder makePostConnectionAndStringBuilder(String apiUrl, String post) {
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            HttpURLConnection conn = makePostConnection(apiUrl, post);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                int i = output.indexOf("}");
                sb.append(output);
            }
            conn.disconnect();
            conn.getResponseMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb;
    }



    //StringBuilder sb=makePostConnectionAndStringBuilder("/modalities/" + aet + "/store", ids.toString());

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

    public void deleteStudyFromOrthanc(BitServerStudy study) throws IOException {
        String fulladdress = "http://" + ipaddress + ":" + port;
        URL url = new URL(fulladdress + "/studies/" + study.getAnonimstudyid());
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

    public HttpURLConnection sendDicom(String apiUrl, byte[] post) {
        HttpURLConnection conn =null;
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
            os.flush();
            conn.getResponseMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

}
