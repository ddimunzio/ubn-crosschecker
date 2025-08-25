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
package org.lw5hr.tool.utils;

import org.lw5hr.model.Qso;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ADIFReader {

    private ArrayList<Qso> list = new ArrayList<Qso>();
    private File file;

    public ADIFReader(File file) {
        this.file = file;
    }

    public ADIFReader(String filePath) {
        this.file = new File(filePath);
    }

    public List<Qso> read() throws Exception {

        ADIFParser parser = new ADIFParser();
        BufferedReader r = new BufferedReader(new FileReader(file));
        String line;
        int i = 1;

        boolean read = false;
        boolean gotRecord = false;
        StringBuffer record = new StringBuffer();

        while ((line = r.readLine()) != null) {

            line = line.trim();

            if (line.toUpperCase().indexOf("<EOH>") > -1) {
                read = true;
            } else {
                if (read) {
                    record.append(line);
                    if (line.toUpperCase().indexOf("<EOR>") > -1) {
                        gotRecord = true;

                    }
                }

            }

            if (gotRecord) {
                try {
                    Qso qso = parser.parseLine(record.toString().replaceAll("\n", "\\n").toUpperCase());
                    if (qso != null) {
                        // qsoPerBand.put(key, value)
                        list.add(qso);
                        setOtherBandsIfExist(qso);
                    }
                } catch (Exception e) {
                    System.out.print(record);
                    e.printStackTrace();
                } finally {
                    record = new StringBuffer();
                    gotRecord = false;
                }
            }

        }

        return list;
    }

    private void setOtherBandsIfExist(Qso q) {

        for (Qso qso : list) {
            if (qso.getBand() != null) {
                if ((qso.getCall().equalsIgnoreCase(q.getCall())) && (!qso.getBand().equalsIgnoreCase(q.getBand()))) {
                    qso.addWorkedAlso(q);
                    q.addWorkedAlso(qso);
                }
            }
        }

    }
}
