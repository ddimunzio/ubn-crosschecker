package org.lw5hr.adif.model;

public class CtyDat {
    private String masterPrefix;

    private String name;

    private Integer CqZone;

    private Integer IaruZone;

    private String continent;

    private Double latitude;

    private Double longitude;

    private Double GmtCorrection;

    public String getMasterPrefix() {
        return masterPrefix;
    }

    public void setMasterPrefix(final String masterPrefix) {
        this.masterPrefix = masterPrefix;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getCqZone() {
        return CqZone;
    }

    public void setCqZone(final Integer cqZone) {
        CqZone = cqZone;
    }

    public Integer getIaruZone() {
        return IaruZone;
    }

    public void setIaruZone(final Integer iaruZone) {
        IaruZone = iaruZone;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(final String continent) {
        this.continent = continent;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Double getGmtCorrection() {
        return GmtCorrection;
    }

    public void setGmtCorrection(final Double gmtCorrection) {
        GmtCorrection = gmtCorrection;
    }
}
