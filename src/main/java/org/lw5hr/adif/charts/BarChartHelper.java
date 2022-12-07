package org.lw5hr.adif.charts;

import java.time.LocalDate;

public class BarChartHelper {

    private Integer value;

    private String rowKey;

    private String ColKey;

    private LocalDate date;


    public Integer value() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    public String rowKey() {
        return rowKey;
    }

    public void setRowKey(final String rowKey) {
        this.rowKey = rowKey;
    }

    public String ColKey() {
        return ColKey;
    }

    public void setColKey(final String colKey) {
        ColKey = colKey;
    }

    public LocalDate date() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }
}
