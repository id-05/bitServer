package ru.bitServer.service;

import ru.bitServer.util.LogTool;
import java.util.ArrayList;

public class DicomRuleParser {

    public StringBuilder textSettings;

    public DicomRuleParser(StringBuilder textSettings){
        this.textSettings = textSettings;
    }

    public ArrayList<DicomRouteRule> getRulesList(){
        boolean hasIf = false;
        String tag = "";
        String tagValue = "";
        ArrayList<DicomRouteRule> dicomRouteRuleList = new ArrayList<>();
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
                    DicomRouteRule bufRule = new DicomRouteRule(tag, tagValue, buf, false);
                    dicomRouteRuleList.add(bufRule);
                    hasIf = false;
                }
            }
        }catch (Exception e){
            LogTool.getLogger().warn("Error getRulesList() DicomRuleParser "+e.getMessage());
        }
        return dicomRouteRuleList;
    }
}

