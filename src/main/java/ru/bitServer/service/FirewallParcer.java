package ru.bitServer.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirewallParcer {

    public StringBuilder textSettings;

    public FirewallParcer(StringBuilder textSettings) {
        this.textSettings = textSettings;
    }

    public ArrayList<FirewallPort> getFirewallList(){
        ArrayList<FirewallPort> portList = new ArrayList<>();
        String[] strings = textSettings.toString().split("\n");
        FirewallPort bufPort;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher;
        for (String str : strings) {
            if(str.contains("$IPTABLES -A INPUT -p tcp --dport ")){
                matcher = pattern.matcher(str);
                matcher.find();
                bufPort = new FirewallPort();
                bufPort.setPort(matcher.group());
                if(str.contains("#")){
                    bufPort.setComment(str.split("#")[1]);
                }
                portList.add(bufPort);

            }
        }

        return portList;
    }

    public StringBuilder updateFirewallFile(ArrayList<FirewallPort> ports){
        StringBuilder output = new StringBuilder();
        String[] strings = textSettings.toString().split("\n");
        boolean flag = false;
        for (String str : strings) {
            if(!str.contains("$IPTABLES -A INPUT -p tcp --dport ")){
                    output.append(str).append("\n");
            }else{
                if(!flag){
                    for(FirewallPort port:ports){
                        output.append("        $IPTABLES -A INPUT -p tcp --dport ").append(port.getPort()).append(" -j ACCEPT  #").append(port.getComment()).append("\n");
                        flag = true;
                    }
                }
            }
        }
        return output;
    }
}
