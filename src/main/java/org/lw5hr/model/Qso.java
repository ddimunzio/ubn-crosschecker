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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.GenericGenerator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;



@ApplicationScoped
@Entity
@Table(name = "QSOS")
public class Qso implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "call")
    private String call;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "band")
    private String band;

    @Column(name = "mode")
    private String mode;

    @Column(name = "CQ")
    private String CQzone;

    @Column(name = "locator")
    private String QTHLocator;

    @Column(name = "state")
    private String state;

    @Column(name = "ITU")
    private String ITUzone;

    @Column(name = "frequeny")
    private String frequency;

    @Column(name = "rRst")
    private String rstReceived;

    @Column(name = "sRst")
    private String rstSent;

    @Column(name = "exchangeTx")
    private String exchangeTx;

    @Column(name = "exchangeRx")
    private String exchangeRx;

    @Column(name = "contestId")
    private String contestId;

    @Column(name = "operator")
    private String operator;

    @Transient
    private Collection<Qso> workedAlso = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Contest contest;

    // Getters and setters...

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCall() { return call; }
    public void setCall(String call) { this.call = call; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getBand() { return band; }
    public void setBand(String band) { this.band = band; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getCQzone() { return CQzone; }
    public void setCQzone(String CQzone) { this.CQzone = CQzone; }

    public String getQTHLocator() { return QTHLocator; }
    public void setQTHLocator(String QTHLocator) { this.QTHLocator = QTHLocator; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getITUzone() { return ITUzone; }
    public void setITUzone(String ITUzone) { this.ITUzone = ITUzone; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getRstReceived() { return rstReceived; }
    public void setRstReceived(String rstReceived) { this.rstReceived = rstReceived; }

    public String getRstSent() { return rstSent; }
    public void setRstSent(String rstSent) { this.rstSent = rstSent; }

    public String getExchangeTx() { return exchangeTx; }
    public void setExchangeTx(String exchangeTx) { this.exchangeTx = exchangeTx; }

    public String getExchangeRx() { return exchangeRx; }
    public void setExchangeRx(String exchangeRx) { this.exchangeRx = exchangeRx; }

    public String getContestId() { return contestId; }
    public void setContestId(String contestId) { this.contestId = contestId; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public void addWorkedAlso(Qso a) { workedAlso.add(a); }
    public Collection<Qso> getWorkedAlso() { return workedAlso; }

    public String getWorkedAlsoAsHTML() {
        StringBuilder buff = new StringBuilder();
        for (Qso qso : workedAlso) {
            buff.append("<b>").append(qso.getBand()).append("</b> ").append(qso.getDateAsHTML()).append("<br>");
        }
        return buff.toString();
    }

    public String getDateAsHTML() {
        return date != null ? date.toString() : "";
    }

    @Override
    public String toString() {
        return frequency + "," + mode + "," + date + "," + time + "," + call + "," + operator;
    }
}