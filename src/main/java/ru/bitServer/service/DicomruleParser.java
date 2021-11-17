package ru.bitServer.service;

import java.util.ArrayList;

public class DicomruleParser {

    public StringBuilder textSettings;

    public DicomruleParser(StringBuilder textSettings){
        this.textSettings = textSettings;
    }

    public ArrayList<DicomrouteRule> getRulesList(){
        ArrayList<DicomrouteRule> dicomrouteRuleList = new ArrayList<>();
        String[] strings = textSettings.toString().split("\n");

        for (String str : strings) {
            if(str.contains("SendToModality")) {
                int i = str.indexOf("'");
                int j = str.lastIndexOf("'");
                String buf = str.substring(i+1,j);
                DicomrouteRule bufRule = new DicomrouteRule(buf,false);
                dicomrouteRuleList.add(bufRule);
            }
        }
        return dicomrouteRuleList;
    }
}

