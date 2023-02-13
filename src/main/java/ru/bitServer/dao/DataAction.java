package ru.bitServer.dao;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static ru.bitServer.beans.MainBean.mainServer;

public interface DataAction extends UserDao{

    default void redirectToOsimis(String sid) throws IOException {
        String HttpOrHttps;
        if(mainServer.getHttpmode().equals("true")){
            HttpOrHttps = "https";
        }else{
            HttpOrHttps = "http";
        }
        String referrer = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
        int i = referrer.indexOf("/bitServer/");
        int j = referrer.indexOf("://");
        String address = referrer.substring(j+3,i);
        if(address.contains(":")){
            BitServerResources bufResources = getBitServerResource("port");
            String port = bufResources.getRvalue();
            int k = address.indexOf(":");
            String addressCutPort = address.substring(0,k);
            PrimeFaces.current().executeScript("window.open('"+HttpOrHttps+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+addressCutPort+":"+port+"/osimis-viewer/app/index.html?study="+sid+"','_blank')");
        }else{
            PrimeFaces.current().executeScript("window.open('"+HttpOrHttps+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+address+"/viewer/osimis-viewer/app/index.html?study="+sid+"','_blank')");
        }
    }

    default void redirectToBitViewer(String instance) throws IOException {
        HttpSession session = SessionUtils.getSession();
        session.setAttribute("study", instance);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/bitviewer.xhtml?study="+instance);
    }

    default void redirectToViewer(String sid) throws IOException {
        if(Boolean.parseBoolean(getBitServerResource("debug").getRvalue())){
            redirectToBitViewer(sid);
        }else{
            redirectToOsimis(sid);
        }
    }

    default byte[] getDicomAsByte(OrthancRestApi connection, String fileName) throws Exception {
        String url="/instances/"+fileName+"/file";
        HttpURLConnection conn = connection.makeGetConnection (url);
        InputStream inputStream = conn.getInputStream();
        return IOUtils.toByteArray(inputStream);
    }

    default JsonObject getJsonFromFile()  {
        OrthancRestApi connection;
        boolean luaRead = true;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            luaRead = Boolean.parseBoolean(getBitServerResource("luaRead").getRvalue());
        }catch (Exception e){
            LogTool.getLogger().error("Error Boolean.parseBoolean(getBitServerResource(\"readOrthancSettingfromLua\").getRvalue()");
        }
        if(luaRead) {
            String urlParameters = "f = io.open(\"" + ModifyStr(mainServer.getPathToJson()) + "orthanc.json\",\"r+\");" +
                    "print(f:read(\"*a\"))" +
                    "f:close()";
            connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
            try {
                stringBuilder = connection.makePostConnectionAndStringBuilderWithIOE("/tools/execute-script", urlParameters);
            }catch (Exception e){
                LogTool.getLogger().error("DataAction:getJsonFromFile error get orthanc.json with lua");
            }

        }else{
            try(FileReader reader = new FileReader(mainServer.getPathToJson()+"orthanc.json")) {
                int c;
                while ((c = reader.read()) != -1) {
                    stringBuilder.append((char) c);
                }
            } catch (Exception e) {
                LogTool.getLogger().error("Error of read file orthanc.json settingsBean: "+e.getMessage());
            }
        }

        JsonParser parser = new JsonParser();
        JsonObject bufJson = new JsonObject();
        try {
            bufJson = parser.parse(stringBuilder.toString()).getAsJsonObject();
        }catch (Exception e){
            e.getStackTrace();
            LogTool.getLogger().warn("Error parse json snapshot: "+stringBuilder.toString()+" "+e.getMessage());
        }
        return bufJson;
    }

}
