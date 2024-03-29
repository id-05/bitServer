package ru.bitServer.service;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkListItemParser {

    String worklisttextFile;
    HashMap<Integer,String> commentMap;

    public WorkListItemParser(String worklisttextFile) {
        this.worklisttextFile = worklisttextFile;
    }

    public ArrayList<WorkListItem> getItemList(){
        ArrayList<WorkListItem> resultList = new ArrayList<>();
        commentMap = new HashMap<>();

        String[] strings = worklisttextFile.split("\n");
        int i = 1;
        for(String buf:strings){
            if(buf.contains("#")|(buf.equals(""))){
                commentMap.put(i,buf);
            }else {
                int firstSpace = buf.indexOf(" ");
                if (firstSpace != -1) {
                    String subBuf = buf.substring(firstSpace+1);
                    int secondSpace = subBuf.indexOf(" ");
                    if (secondSpace != -1) {
                        WorkListItem bufItem = new WorkListItem(buf.substring(0, firstSpace), subBuf.substring(0, secondSpace), subBuf.substring(secondSpace));
                        resultList.add(bufItem);
                    }
                }
            }
            i++;
        }

        return resultList;
    }
}
