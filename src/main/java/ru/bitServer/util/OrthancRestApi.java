package ru.bitServer.util;

import ru.bitServer.dao.BitServerStudy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import static ru.bitServer.beans.MainBean.mainServer;

public class OrthancRestApi {

    private String authentication;
    private String ipaddress;
    private String port;
    private String login;
    private String password;


    public OrthancRestApi(String ipaddress, String port, String login, String password){
        this.ipaddress = ipaddress;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public StringBuilder makeGetConnectionAndStringBuilder(String apiUrl) {
        StringBuilder stringBuilder = null ;
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
        HttpURLConnection conn  = null;
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
        StringBuilder sb = null;
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

    public HttpURLConnection makePostConnection(String apiUrl, String post) throws Exception {
        String fulladdress = "http://" + ipaddress + ":" + port;
        HttpURLConnection conn = null;
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
        conn.getResponseMessage();
        return conn;
    }

    public void deleteStudyFromOrthanc(BitServerStudy study) throws IOException {
        String fulladdress = "http://" + mainServer.getIpaddress() + ":" + mainServer.getPort();
        URL url = new URL(fulladdress + "/studies/" + study.getAnonimstudyid());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("DELETE");
        authentication = Base64.getEncoder().encodeToString((mainServer.getLogin() + ":" + mainServer.getPassword()).getBytes());
        if(this.authentication != null){
            conn.setRequestProperty("Authorization", "Basic " + this.authentication);
        }
        conn.getResponseMessage();
        conn.disconnect();
    }

}
