package com.whisht.heatapp.database.data;

public class StationData {
    private String stationId;
    private String stationName;
    private String stationPid;
    private String stationJP;
    private String stationShort;
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationPid() {
        return stationPid;
    }

    public void setStationPid(String stationPid) {
        this.stationPid = stationPid;
    }

    public String getStationJP() {
        return stationJP;
    }

    public void setStationJP(String stationJP) {
        this.stationJP = stationJP;
    }

    public String getStationShort() {
        return stationShort;
    }

    public void setStationShort(String stationShort) {
        this.stationShort = stationShort;
    }

    public void parse() {
        if(stationShort == null)
            stationShort = "";
    }
}
