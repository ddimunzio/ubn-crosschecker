package org.lw5hr.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UbnResult
{
    private ArrayList<Qso> notInLog = new ArrayList<>();
    private ArrayList<Qso> incorrectCall = new ArrayList<>();
    private ArrayList<Qso> incorrectExchangeInfo = new ArrayList<>();
    Map<String, Long> totalByOperator;
    List<OperatorErrorStats> operatorErrorStats = new ArrayList<>();

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

    public Map<String, Long> getTotalByOperator() {
        return totalByOperator;
    }
    public void setTotalByOperator(Map<String, Long> totalByOperator) {
        this.totalByOperator = totalByOperator;
    }

    public List<OperatorErrorStats> getOperatorErrorStats() {
        return operatorErrorStats;
    }

    public void setOperatorErrorStats(List<OperatorErrorStats> operatorErrorStats) {
        this.operatorErrorStats = operatorErrorStats;
    }
}
