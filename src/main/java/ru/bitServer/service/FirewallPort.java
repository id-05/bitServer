package ru.bitServer.service;

public class FirewallPort {
    String port;
    String comment;

    public FirewallPort(String port, String comment) {
        this.port = port;
        this.comment = comment;
    }

    public FirewallPort() {

    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
