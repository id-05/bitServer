package ru.bitServer.beans;

public class NetworkAdapter {

    private String name;
    private String ipaddress;
    private String mask;
    private String gateway;
    private String dns;
    private String mode;
    private String automode;

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

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAutomode() {
        return automode;
    }

    public void setAutomode(String automode) {
        this.automode = automode;
    }

    public NetworkAdapter(){

    }

    public NetworkAdapter(String name, String ipaddress, String mask, String gateway, String dns, String mode, String automode){
        this.name = name;
        this.ipaddress = ipaddress;
        this.mask = mask;
        this.gateway = gateway;
        this.dns = dns;
        this.mode = mode;
        this.automode = automode;
    }
}
