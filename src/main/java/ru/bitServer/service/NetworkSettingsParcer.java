package ru.bitServer.service;

import java.util.ArrayList;

public class NetworkSettingsParcer {

    public StringBuilder textSettings;

    public NetworkSettingsParcer(StringBuilder textSettings){
        this.textSettings = textSettings;
    }

    public ArrayList<NetworkAdapter> getAdapterList(){
        ArrayList<NetworkAdapter> adapterList = new ArrayList<>();
        String[] strings = textSettings.toString().split("\n");
        boolean firstteg = true;
        NetworkAdapter bufAdapter = null;
        for (String str : strings) {
            if(str.contains("iface")){
                if(!str.contains("lo")){

                    String[] subStrings = str.split(" ");

                    if (firstteg) {
                        bufAdapter = new NetworkAdapter();
                        bufAdapter.setName(subStrings[1]);
                        if(subStrings[3].contains("static")){
                            bufAdapter.setIpMode("static");
                        }
                        if(subStrings[3].contains("dhcp")){
                            bufAdapter.setIpMode("dhcp");
                        }
                        firstteg = false;
                    } else {
                        adapterList.add(bufAdapter);
                        bufAdapter = new NetworkAdapter();
                        bufAdapter.setName(subStrings[1]);
                        if(subStrings[3].contains("static")){
                            bufAdapter.setIpMode("static");
                        }
                        if(subStrings[3].contains("dhcp")){
                            bufAdapter.setIpMode("dhcp");
                        }
                    }
                }
            }

            if(str.contains("address")){
                String[] subStrings = str.split(" ");
                assert bufAdapter != null;
                bufAdapter.setIpaddress(subStrings[1]);
            }

            if(str.contains("netmask")){
                String[] subStrings = str.split(" ");
                assert bufAdapter != null;
                bufAdapter.setMask(subStrings[1]);
            }

            if(str.contains("gateway")){
                String[] subStrings = str.split(" ");
                assert bufAdapter != null;
                bufAdapter.setGateway(subStrings[1]);
            }
        }
        if (bufAdapter!=null) {
            adapterList.add(bufAdapter);
        }
        return adapterList;
    }
}
