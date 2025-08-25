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

public class OperatorErrorStats {
    private String operator;
    private long total;
    private long invalidCalls;
    private long badExchanges;
    private long notInLog;
    private long totalErrors;
    private double percentage;

    public OperatorErrorStats() {
    }

    // Constructor, getters, setters
    public OperatorErrorStats(String operator, long total, long invalidCalls, long badExchanges, long notInLog, long totalErrors, double percentage) {
        this.operator = operator;
        this.total = total;
        this.invalidCalls = invalidCalls;
        this.badExchanges = badExchanges;
        this.notInLog = notInLog;
        this.totalErrors = totalErrors;
        this.percentage = percentage;
    }


    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getInvalidCalls() {
        return invalidCalls;
    }

    public void setInvalidCalls(long invalidCalls) {
        this.invalidCalls = invalidCalls;
    }

    public long getBadExchanges() {
        return badExchanges;
    }

    public void setBadExchanges(long badExchanges) {
        this.badExchanges = badExchanges;
    }

    public long getNotInLog() {
        return notInLog;
    }

    public void setNotInLog(long notInLog) {
        this.notInLog = notInLog;
    }

    public long getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(long totalErrors) {
        this.totalErrors = totalErrors;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}