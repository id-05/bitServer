package ru.bitServer.service;

import ru.bitServer.util.LogTool;
import java.util.ArrayList;

public class DicomruleParser {

    public StringBuilder textSettings;

    public DicomruleParser(StringBuilder textSettings){
        this.textSettings = textSettings;
    }

    public ArrayList<DicomrouteRule> getRulesList(){
        boolean hasIf = false;
        String tag = "";
        String tagValue = "";
        ArrayList<DicomrouteRule> dicomrouteRuleList = new ArrayList<>();
        String[] strings = textSettings.toString().split("\n");
        try {
            for (String str : strings) {
                if (str.contains("if tags.")) {
                    hasIf = true;
                    int tagStart = str.indexOf("if tags.") + 8;
                    int tagStop = str.indexOf("==") - 1;
                    tag = str.substring(tagStart, tagStop);
                    int tagValueStart = str.indexOf("'") + 1;
                    int tagValueStop = str.lastIndexOf("'");
                    tagValue = str.substring(tagValueStart, tagValueStop);
                }
                if (str.contains("SendToModality")) {
                    int i = str.indexOf("'");
                    int j = str.lastIndexOf("'");
                    String buf = str.substring(i + 1, j);
                    if (!hasIf) {
                        tag = "all";
                        tagValue = "all";
                    }
                    DicomrouteRule bufRule = new DicomrouteRule(tag, tagValue, buf, false);
                    dicomrouteRuleList.add(bufRule);
                    hasIf = false;
                }
            }
        }catch (Exception e){
            LogTool.getLogger().warn("Error getRulesList() DicomRuleParser "+e.getMessage());
        }
        return dicomrouteRuleList;
    }
}

