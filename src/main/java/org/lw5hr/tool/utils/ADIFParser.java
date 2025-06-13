package org.lw5hr.tool.utils;

import org.lw5hr.model.Qso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

//import org.hamradio.lw4hbr.derby.DerbyManager;
public class ADIFParser {

    private static final DateFormat formater = new SimpleDateFormat("yyyyMMdd HHmmss");

    public Qso parseLine(String line) {

        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        Integer pos = 0;
        String field = null;
        Integer start = null;
        Integer end = null;
        Integer twoPoints = null;
        Integer leng = null;
        String value = null;

        while (pos < line.length()) {

            if (line.charAt(pos) == '<') {
                start = pos;
            }

            if ((line.charAt(pos) == ':') && (twoPoints == null)) {
                twoPoints = pos;
            }

            if (line.charAt(pos) == '>') {
                end = pos;
            }

            if (end != null) {
                if ((twoPoints != null) && (twoPoints > start)) {
                    field = line.substring(start + 1, twoPoints);
                    if (field.indexOf("DATE") > -1) {
                        leng = Integer.parseInt(line.substring(twoPoints + 1, twoPoints + 2));
                    } else {
                        leng = Integer.parseInt(line.substring(twoPoints + 1, end));
                    }

                    value = line.substring(end + 1, end + 1 + leng);
                    fieldsValues.put(field, value);
                    //pos=(end + 1 + leng);
                } else {
                    // EOH or EOR
                    field = line.substring(start + 1, end);
                }
                start = null;
                end = null;
                twoPoints = null;
                leng = null;
                value = null;
                field = null;
            }

            pos++;
        }

        return getQSO(fieldsValues);
    }

    // <CALL:6>YY5OGI <QSO_DATE:8>19980402 <TIME_ON:6>021500 <BAND:3>10M
    // <CONTEST_ID:2>DX <FREQ_RX:8>28.00000 <MODE:3>SSB <RST_RCVD:2>59
    // <RST_SENT:2>59 <OPERATOR:1> <CQZ:1>9 <APP_N1MM_POINTS:1>1
    // <APP_N1MM_RADIO_NR:1>0 <APP_N1MM_RUN1RUN2:1>0
    // <APP_N1MM_RADIOINTERFACED:1>1 <EOR>
    /**
     * QSO_DATE=20110709, APP_N1MM_POINTS=3, CONTEST_ID=IARU-HF,
     * FREQ_RX=14.30100, STX=3, ITUZ=15, OPERATOR=LR5H,
     * APP_N1MM_RADIOINTERFACED=1, CQZ=11, APP_N1MM_RADIO_NR=0, TIME_ON=120412,
     * FREQ=14.30100, CALL=PY4COM, RST_SENT=59, APP_N1MM_RUN1RUN2=1, BAND=20M,
     * RST_RCVD=59, MODE=SSB}
     *
     * @param fieldsValues
     * @return
     */
    private Qso getQSO(HashMap<String, String> fieldsValues) {

        Qso qso = null;
        try {
            if (fieldsValues.size() > 0) {
                qso = new Qso();

                qso.setCall(fieldsValues.get("CALL").trim());

                if (fieldsValues.get("QSO_DATE") != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
                    LocalDate qsoDate = LocalDate.parse(fieldsValues.get("QSO_DATE"), formatter);
                    qso.setDate(qsoDate);
                }
                if (fieldsValues.get("TIME_ON") != null) {
                    String timeOn = fieldsValues.get("TIME_ON");
                    DateTimeFormatter formatter;
                    if (timeOn.length() > 4) {
                        formatter = DateTimeFormatter.ofPattern("HHmmss");
                    } else {
                        formatter = DateTimeFormatter.ofPattern("HHmm");
                    }
                    qso.setTime(LocalTime.parse(fieldsValues.get("TIME_ON"), formatter));
                }


                if (fieldsValues.get("BAND") != null) {
                    qso.setBand(fieldsValues.get("BAND").trim());
                }

                if (fieldsValues.get("CQZ") != null) {
                    qso.setCQzone(fieldsValues.get("CQZ").trim());
                }

                if (fieldsValues.get("STX") != null) {
                    qso.setExchangeTx(fieldsValues.get("STX").trim());
                }

                if (fieldsValues.get("SRX") != null) {
                    qso.setExchangeRx(fieldsValues.get("SRX").trim());
                }

                if (fieldsValues.get("MODE") != null) {
                    qso.setMode(fieldsValues.get("MODE").trim());
                }
                if (fieldsValues.get("OPERATOR") != null) {
                    qso.setOperator(fieldsValues.get("OPERATOR").trim());
                } else {
                    qso.setOperator("N/A");
                }

                if (fieldsValues.get("ITUZ") != null) {
                    qso.setITUzone(fieldsValues.get("ITUZ").trim());

                }
                if (fieldsValues.get("FREQ") != null) {
                    qso.setFrequency(fieldsValues.get("FREQ").trim());

                }
                if (fieldsValues.get("RST_RCVD") != null) {
                    qso.setRstReceived(fieldsValues.get("RST_RCVD").trim());

                }
                if (fieldsValues.get("RST_SENT") != null) {
                    qso.setRstSent(fieldsValues.get("RST_SENT").trim());

                }
                if (fieldsValues.get("CONTEST_ID") != null) {
                    qso.setContestId(fieldsValues.get("CONTEST_ID").trim());

                }
                if (fieldsValues.get("GRIDSQUARE") != null) {
                    qso.setQTHLocator(fieldsValues.get("GRIDSQUARE").trim());

                }

                if (fieldsValues.get("STATE") != null) {
                    qso.setState(fieldsValues.get("STATE").trim());

                }

                if (fieldsValues.get("OPERATOR") != null) {
                    qso.setOperator(fieldsValues.get("OPERATOR").trim());

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qso;
    }
}
