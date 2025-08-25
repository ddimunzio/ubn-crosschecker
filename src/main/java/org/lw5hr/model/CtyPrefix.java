/*
 * UBN Cross Checker - Amateur Radio Contest Log Analysis Tool
 *
 * Copyright (c) 2025 LW5HR
 *
 * This software is provided free of charge for the amateur radio community.
 * Feel free to use, modify, and distribute.
 *
 * @author LW5HR
 * @version 1.0
 * @since 2025
 *
 * 73!
 */
package org.lw5hr.model;

public class CtyPrefix {

    private String prefix;

    private String masterPrefix;

    private Integer CqZone;

    private Integer IaruZone;

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
}
