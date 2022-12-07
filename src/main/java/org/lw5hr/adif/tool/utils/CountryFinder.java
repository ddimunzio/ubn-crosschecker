/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.lw5hr.adif.tool.utils;

import org.lw5hr.adif.model.CtyDat;
import org.lw5hr.adif.model.CtyPrefix;
import org.lw5hr.adif.model.Prefixes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author diego
 */
public class CountryFinder {

    private static final List<CtyDat> ctyDatList = new ArrayList<>();
    private static final List<CtyPrefix> ctyPrefixList = new ArrayList<>();
    private static final List<Prefixes> prefixesList = new ArrayList<>();

    static {
        String rootPath = new java.io.File("").getAbsolutePath() + "/cty.dat";
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(rootPath));
            scanner.useDelimiter(";");
            /* COLUMN LENGTH   DESCRIPTION */
            /* 1        26      Country Name */
            /* 27       5       CQ Zone */
            /* 32       5       ITU Zone */
            /* 37       5       2-letter continent abbreviation */
            /* 42       9       Latitude in degrees, + for North */
            /* 51       10      Longitude in degrees, + for West */
            /* 61       9       Local time offset from GMT */
            /* 70       6       Primary DXCC Prefix (A “*” preceding this prefix indicates */
            //that the country is on the DARC WAEDC list, and counts in */
            //CQ-sponsored contests, but not ARRL-sponsored contests).*/

            while (scanner.hasNext()) {
                String st = scanner.next();
                if (st.length() > 2) {
                    String[] fields = st.split(":");
                    final String[] extraData = fields[8].split(",");
                    CtyDat ctyDat = new CtyDat();

                    ctyDat.setName(fields[0].replace("\r\n", "").trim());
                    ctyDat.setCqZone(Integer.parseInt(fields[1].trim()));
                    ctyDat.setIaruZone(Integer.parseInt(fields[2].trim()));
                    ctyDat.setContinent(fields[3]);
                    ctyDat.setLatitude(Double.parseDouble(fields[4].trim()));
                    ctyDat.setLongitude(Double.parseDouble(fields[5].trim()));
                    ctyDat.setGmtCorrection(Double.parseDouble(fields[6]));
                    ctyDat.setMasterPrefix(fields[7].trim());

                    /*
                        (#)   Override CQ Zone
                        [#]   Override ITU Zone
                        <#/#> Override latitude/longitude
                        {aa}  Override Continent
                        ~#~	Override local time offset from GMT
                     */

                    for (String p : extraData) {
                        CtyPrefix ctyPrefix = new CtyPrefix();
                        Prefixes prefix = new Prefixes();
                        ctyPrefix.setMasterPrefix(ctyDat.getMasterPrefix().replace("\r\n", "").trim());
                        ctyPrefix.setPrefix(p.replace("\r\n", "").trim());
                        prefix.setMasterPrefix(ctyDat.getMasterPrefix());

                        if (p.contains("=")) {
                            prefix.setPrefix(p.replace("=", "").replace("\r\n", "").trim());
                            prefix.setCall(true);
                        } else {
                            prefix.setPrefix(p);
                        }
                        if (p.contains("(")) {
                            ctyPrefix.setCqZone(Integer.valueOf(p.substring(p.indexOf("(") + 1, p.indexOf(")"))));
                        } else {
                            ctyPrefix.setCqZone(ctyDat.getCqZone());
                        }
                        if (p.contains("[")) {
                            ctyPrefix.setIaruZone(Integer.valueOf(p.substring(p.indexOf("[") + 1, p.indexOf("]"))));

                        } else {
                            ctyPrefix.setIaruZone(ctyDat.getIaruZone());
                        }
                        prefixesList.add(prefix);
                        ctyPrefixList.add(ctyPrefix);
                    }

                    ctyDatList.add(ctyDat);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CountryFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static AbstractMap<String, CtyDat> getCountryFromCall(String call) {
        HashMap<String, CtyDat> results = new HashMap<>();
        final boolean isSlash = call.indexOf("/") > 1 && call.indexOf("/") < 4;

        Optional<CtyDat> isCty;
        if (isSlash) {
            isCty = ctyDatList.stream()
                    .filter(ct -> ct.getMasterPrefix().matches(call.substring(0, call.indexOf("/"))))
                    .findFirst();
        } else {
            isCty = ctyDatList.stream()
                    .filter(c -> call.toUpperCase().startsWith(c.getMasterPrefix())).findFirst();
        }

        isCty.ifPresent(ctyDat -> results.put(call, ctyDat));

        final Optional<CtyPrefix> isCtyPrefix = ctyPrefixList.stream()
                .filter(cp -> call.toUpperCase().startsWith(cp.prefix())).findFirst();
        isCtyPrefix.ifPresent(ctyPrefix -> results.put(call, ctyDatList.stream()
                .filter(c -> c.getMasterPrefix().equalsIgnoreCase(ctyPrefix.masterPrefix())).findFirst().get()));

        final Optional<Prefixes> isPrefix = prefixesList.stream().filter(p -> call.equalsIgnoreCase(p.prefix())).findFirst();
        isPrefix.ifPresent(prefixes -> results.put(call, ctyDatList.stream()
                .filter(c -> c.getMasterPrefix().equalsIgnoreCase(prefixes.masterPrefix())).findFirst().get()));

        return results;
    }
}
