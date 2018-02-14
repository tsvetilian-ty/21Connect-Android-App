package com.ty.tsvetilian.a21connect.Models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Device extends RealmObject {

    @PrimaryKey
    @Required
    private String hash;
    private String hostName;
    private String ip;
    private int ram;
    private String cpu;
    private String osType;
    private String authToken;
    private boolean isSyncEnabled;

    public Device() {
    }

    public Device(String hash, String hostName, String ip, int ram, String cpu, String osType, String authToken, boolean sync) {
        this.hash = hash;
        this.hostName = hostName;
        this.ip = ip;
        this.ram = ram;
        this.cpu = cpu;
        this.osType = osType;
        this.authToken = authToken;
        this.isSyncEnabled = sync;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isSyncEnabled() {
        return isSyncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        isSyncEnabled = syncEnabled;
    }
}
