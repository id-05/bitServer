package ru.bitServer.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import ru.bitServer.dicom.OrthancStudy;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import javax.faces.context.FacesContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static ru.bitServer.beans.MainBean.mainServer;

public interface DataAction extends UserDao{

    default int syncDataBase(OrthancRestApi connection) throws SQLException {

//        double startTime = new Date().getTime();
//        JsonParser parserJson = new JsonParser();
//        JsonArray studies = (JsonArray) parserJson.parse(connection.makeGetConnectionAndStringBuilder("/studies/").toString());
//        Iterator<JsonElement> studiesIterator = studies.iterator();
//        ArrayList<String> idOrthancStudy = new ArrayList<>();
//        while (studiesIterator.hasNext()) {
//            String studyData =  studiesIterator.next().getAsString();
//            idOrthancStudy.add(studyData);
//        }
//        double readyExistOrthancStudyList = ((new Date().getTime())-startTime)/1000;
//
//        startTime = new Date().getTime();
//        ArrayList<String> bufStudyList = new ArrayList<>();
//        for(BitServerStudy bufStudy:getAllBitServerStudyOnlyId()){
//            bufStudyList.add(bufStudy.getSid());
//        }
//        double readyExistBitServerStudyList = ((new Date().getTime())-startTime)/1000;
//
//        startTime = new Date().getTime();
//        idOrthancStudy.removeAll(bufStudyList);
//        double readyOnlyNewStudyList = ((new Date().getTime())-startTime)/1000;
//
//        startTime = new Date().getTime();
//        int i = 0;
//        int size = idOrthancStudy.size();
//        for(String bufId:idOrthancStudy){
//            StringBuilder sb = connection.makeGetConnectionAndStringBuilder("/studies/"+bufId);
//            parserJson = new JsonParser();
//            JsonObject jsonObject = (JsonObject) parserJson.parse(sb.toString());
//            OrthancStudy bufStudy = connection.parseStudy(jsonObject);
//           // if(!bufStudy.getPatientName().equals("ANONIM")){
//                BitServerStudy buf = new BitServerStudy(bufStudy.getOrthancId(), bufStudy.getShortId(), bufStudy.getStudyDescription(),
//                        bufStudy.getInstitutionName(), bufStudy.getDate(),
//                        bufStudy.getModality(), new Date(), bufStudy.getPatientName(), bufStudy.getPatientBirthDate(), bufStudy.getPatientSex(), " ", " ", 0);
//                addStudyJDBC(buf);
//                i++;
//           // }
//        }
//        double finishAdd = ((new Date().getTime())-startTime)/1000;
//        if(getBitServerResource("debug").getRvalue().equals("true")){
//        LogTool.getLogger().info(this.getClass().getSimpleName()+": "+ "Получение study orthanc: "+readyExistOrthancStudyList+
//                "c. Получение study bitServer: "+readyExistBitServerStudyList+
//                "c. Новых: "+i+
//                " Обработока: "+readyOnlyNewStudyList+
//                "c. Запись в базу: "+finishAdd+"c."+
//                "Всего новых и анонимных: "+size);
//        }
        int i =0;
        return i;
    }

    public default void redirectToOsimis(String sid) {
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

}
