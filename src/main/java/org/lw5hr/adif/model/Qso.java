package org.lw5hr.adif.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

public class Qso {

    private String call;
    private LocalDate date;
    private LocalTime time;
    private String band;
    private String mode;
    private String CQzone;
    private String QTHLocator;
    private String state;
    private String ITUzone;
    private String frequency;
    private String rstReceived;
    private String rstSent;

    private String exchangeTx;

    private String exchangeRx;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getQTHLocator() {
        return QTHLocator;
    }

    public void setQTHLocator(String qTHLocator) {
        QTHLocator = qTHLocator;
    }

    public String getITUzone() {
        return ITUzone;
    }

    public void setITUzone(String iTUzone) {
        ITUzone = iTUzone;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRstReceived() {
        return rstReceived;
    }

    public void setRstReceived(String rstReceived) {
        this.rstReceived = rstReceived;
    }

    public String getRstSent() {
        return rstSent;
    }

    public void setRstSent(String rstSent) {
        this.rstSent = rstSent;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    String contestId;

    Collection<Qso> workedAlso = new ArrayList<Qso>();
    String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void addWorkedAlso(Qso a) {
        workedAlso.add(a);
    }

    public Collection<Qso> getOworkedAlso() {
        return workedAlso;
    }

    public String getOworkedAlsoAsHTML() {
        StringBuffer buff = new StringBuffer();
        for (Qso qso : workedAlso) {
            buff.append("<b>" + qso.getBand() + "</b> " + qso.getDateAsHTML() + "<br>");
        }
        return buff.toString();
    }

   @Override
	public String toString() {
		return  frequency + "," + mode + "," + date + "," + time + "," + call + "," + operator;
	}

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateAsHTML() {
        return new SimpleDateFormat("E hh:mm:ss").format(date);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCQzone() {
        return CQzone;
    }

    public void setCQzone(String qzone) {
        CQzone = qzone;
    }

    public String exchangeTx() {
        return exchangeTx;
    }

    public void setExchangeTx(final String exchangeTx) {
        this.exchangeTx = exchangeTx;
    }

    public String exchangeRx() {
        return exchangeRx;
    }

    public void setExchangeRx(final String exchangeRx) {
        this.exchangeRx = exchangeRx;
    }

    public String getQsoAsHTML() {

        StringBuffer html = new StringBuffer();
        html.append("<b><font color='#CC0000' size='+3'>$[name]</font></b><br>" + "<table width='300' border='0'>");

        if (operator != null) {
            html.append("<tr><td><b>Operator</b></td><td>" + operator + "</td></tr>");
        }

        if (date != null) {
            html.append("<tr><td><b>Time</b></td><td>" + getDateAsHTML() + "</td></tr>");
        }

        if (frequency != null) {
            html.append("<tr><td><b>Frequency</b></td><td>" + getFrequency() + "</td>" + "</tr>");
        }

        if (band != null) {
            html.append("<tr><td><b>Band</b></td><td>" + getBand() + "</td>" + "</tr>");
        }

        if (CQzone != null) {
            html.append("<tr><td><b>CQ ZONE</b></td><td>" + getCQzone() + "</td>" + "</tr>");
        }

        if (ITUzone != null) {
            html.append("<tr><td><b>ITU ZONE</b></td><td>" + getITUzone() + "</td>" + "</tr>");
        }


        //if (country != null)
        //	html.append("<tr><td><b>Country</b></td><td>" + (getCountry() != null ? getCountry().getName() : "NA") + "</td>" + "</tr>");
        if (QTHLocator != null) {
            html.append("<tr><td><b>QTH Loc</b></td><td>" + getQTHLocator().toUpperCase() + "</td>" + "</tr>");
        }

        if (mode != null) {
            html.append("<tr><td><b>Mode</b></td><td>" + getMode() + "</td>" + "</tr>");
        }

        if (frequency != null) {
            html.append("<tr><td><b>Frequency</b></td><td>" + getFrequency() + "</td>" + "</tr>");
        }

        if (rstReceived != null) {
            html.append("<tr><td><b>RST Received</b></td><td>" + getRstReceived() + "</td>" + "</tr>");
        }

        if (rstSent != null) {
            html.append("<tr><td><b>RST Sent</b></td><td>" + getRstSent() + "</td>" + "</tr>");
        }

        if (workedAlso.size() > 0) {
            html.append("<tr><td><b>Also Worked on</b></td><td>" + getOworkedAlsoAsHTML() + "</td>" + "</tr>");
        }

        html.append("</table>" + "<br>" + "<a href='http://www.qrz.com/db/$[name]'>Open $[name] in QRZ.com</a>");

        return html.toString();
    }

   /* public int getQsoHour() {
        final String hour = this.date.toInstant()                                // Capture current moment in UTC.
                .truncatedTo(ChronoUnit.SECONDS)            // Lop off any fractional second.
                .plus(8, ChronoUnit.HOURS)       // Add eight hours.
                .atZone(ZoneId.of("UTC"))    // Adjust from UTC to the wall-clock time used by the people of a certain region (a time zone). Returns a `ZonedDateTime` object.
                .format(                                      // Generate a `String` object representing textually the value of the `ZonedDateTime` object.
                        DateTimeFormatter.ofPattern("HH")
                                .withLocale(Locale.US) // Specify a `Locale` to determine the human language and cultural norms used in localizing the text being generated.
                );
        return Integer.parseInt(hour);
    }*/
}
