package ru.bitServer.service;

public class NetworkAdapter {

    private String name;
    private String ipaddress;
    private String mask;
    private String gateway;
    private String ipmode;

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpmode() {
        return ipmode;
    }

    public void setIpmode(String ipmode) {
        this.ipmode = ipmode;
    }

    public NetworkAdapter(){

    }

    public NetworkAdapter(String name, String ipaddress, String mask, String gateway, String ipmode){
        this.name = name;
        this.ipaddress = ipaddress;
        this.mask = mask;
        this.gateway = gateway;
        this.ipmode = ipmode;
    }

    @Override
    public String toString() {
        return "name = "+name+"; ipaddress = "+ipaddress+"; mask = "+mask+"; gateway = "+gateway+"; ipmode = "+ipmode;
    }
}
