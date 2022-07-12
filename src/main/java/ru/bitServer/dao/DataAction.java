package ru.bitServer.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.bitServer.dicom.OrthancStudy;
import ru.bitServer.util.OrthancRestApi;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public interface DataAction extends UserDao{

    default int syncDataBase(OrthancRestApi connection) {

        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(connection.makeGetConnectionAndStringBuilder("/studies/").toString());
        Iterator<JsonElement> studiesIterator = studies.iterator();

        ArrayList<String> idOrthancStudy = new ArrayList<>();
        while (studiesIterator.hasNext()) {
            String studyData =  studiesIterator.next().getAsString();
            idOrthancStudy.add(studyData);
        }

        ArrayList<String> bufStudyList = new ArrayList<>();
        for(BitServerStudy bufStudy:getAllBitServerStudy()){
            bufStudyList.add(bufStudy.getSid());
        }

        idOrthancStudy.removeAll(bufStudyList);
        int i = 0;
        for(String bufId:idOrthancStudy){
            StringBuilder sb = connection.makeGetConnectionAndStringBuilder("/studies/"+bufId);
            parserJson = new JsonParser();
            JsonObject jsonObject = (JsonObject) parserJson.parse(sb.toString());
            OrthancStudy bufStudy = connection.parseStudy(jsonObject);
            if(!bufStudy.getPatientName().equals("ANONIM")){
                BitServerStudy buf = new BitServerStudy(bufStudy.getOrthancId(), bufStudy.getShortId(), bufStudy.getStudyDescription(),
                        bufStudy.getInstitutionName(), bufStudy.getDate(),
                        bufStudy.getModality(), new Date(), bufStudy.getPatientName(), bufStudy.getPatientBirthDate(), bufStudy.getPatientSex(), "", "", 0);
                addStudy(buf);
                i++;
            }
        }
        return i;
    }

}
