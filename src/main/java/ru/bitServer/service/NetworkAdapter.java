package ru.bitServer.service;

public class NetworkAdapter {

    private String name;
    private String ipaddress;
    private String mask;
    private String gateway;
    private String ipMode;

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

    public String getIpMode() {
        return ipMode;
    }

    public void setIpMode(String ipMode) {
        this.ipMode = ipMode;
    }

    public NetworkAdapter(){

    }

    @Override
    public String toString() {
        return "name = "+name+"; ipaddress = "+ipaddress+"; mask = "+mask+"; gateway = "+gateway+"; ipMode = "+ipMode;
    }
}
