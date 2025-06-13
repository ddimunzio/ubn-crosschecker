package org.lw5hr.model;

import java.util.ArrayList;

public class UbnResult
{
    private ArrayList<Qso> notInLog = new ArrayList<>();
    private ArrayList<Qso> incorrectCall = new ArrayList<>();
    private ArrayList<Qso> incorrectExchangeInfo = new ArrayList<>();


    public ArrayList<Qso> getNotInLog() {
        return notInLog;
    }

    public void setNotInLog(ArrayList<Qso> notInLog) {
        this.notInLog = notInLog;
    }

    public ArrayList<Qso> getIncorrectCall() {
        return incorrectCall;
    }

    public void setIncorrectCall(ArrayList<Qso> incorrectCall) {
        this.incorrectCall = incorrectCall;
    }

    public ArrayList<Qso> getIncorrectExchangeInfo() {
        return incorrectExchangeInfo;
    }

    public void setIncorrectExchangeInfo(ArrayList<Qso> incorrectExchangeInfo) {
        this.incorrectExchangeInfo = incorrectExchangeInfo;
    }
}
