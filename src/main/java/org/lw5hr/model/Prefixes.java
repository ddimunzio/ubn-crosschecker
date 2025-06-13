package org.lw5hr.model;

public class Prefixes {

    private String prefix;

    private String masterPrefix;

    private Integer CqZone;

    private Integer IaruZone;

    private Boolean isCall;

    public String prefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public String masterPrefix() {
        return masterPrefix;
    }

    public void setMasterPrefix(final String masterPrefix) {
        this.masterPrefix = masterPrefix;
    }

    public Integer CqZone() {
        return CqZone;
    }

    public void setCqZone(final Integer cqZone) {
        CqZone = cqZone;
    }

    public Integer IaruZone() {
        return IaruZone;
    }

    public void setIaruZone(final Integer iaruZone) {
        IaruZone = iaruZone;
    }

    public Boolean isCall() {
        return isCall;
    }

    public void setCall(final Boolean call) {
        isCall = call;
    }
}
