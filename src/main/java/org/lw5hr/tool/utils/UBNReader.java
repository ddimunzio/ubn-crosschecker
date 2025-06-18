package org.lw5hr.tool.utils;

import org.lw5hr.model.OperatorErrorStats;
import org.lw5hr.model.Qso;
import org.lw5hr.model.UbnResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author diego
 */
public class UBNReader {
    public UbnResult readUbnFile(File filePath, List<Qso> qsos) {
        List<String> result = new ArrayList<>();
        UbnResult ubnResult = new UbnResult();
        BufferedReader reader;
        final Integer NIL = 1;
        final Integer INCORRECT_CALL = 2;
        final Integer INCORRECT_EXCHANGE_INFO = 3;
        final int BAND_CHANGE_VIOLATION = 4;
        final Integer UNIQUE_CALL = 5;
        final int LOST_MULTI = 5;

       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        Integer currentReport = 0;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            while (line != null) {
                if (currentReport != 0 && !line.isEmpty() && !line.startsWith("***")) {
                    List<String> ubnLine = Arrays.stream(line.split(" ")).filter(a -> a != "").toList();
                    LocalDateTime UbnDateTime = LocalDateTime.parse(ubnLine.get(2) + " " + ubnLine.get(3), formatter);
                    Optional<Qso> qso = qsos.stream()
                            .filter(q -> q.getCall().equalsIgnoreCase(ubnLine.get(6)))
                            .filter(q -> UbnDateTime.getHour() == q.getTime().getHour())
                            .filter(q -> UbnDateTime.getMinute() == q.getTime().getMinute()).findFirst();
                    if (qso.isPresent()) {
                        if (currentReport.equals(INCORRECT_CALL)) {
                            System.out.println(qso.get().toString() + "," + ubnLine.get(9));
                            result.add(qso.get().toString() + "," + ubnLine.get(9));
                            ubnResult.getIncorrectCall().add(qso.get());
                        } else if (currentReport.equals(NIL)) {
                            System.out.println(qso.get().toString() + "," + ubnLine.get(6));
                            result.add(qso.get().toString() + "," + ubnLine.get(6));
                            ubnResult.getNotInLog().add(qso.get());
                        } else if (currentReport.equals(INCORRECT_EXCHANGE_INFO)) {
                            System.out.println(qso.get().toString() + "," + ubnLine.get(7) + "," + ubnLine.get(9));
                            result.add(qso.get().toString() + "," + ubnLine.get(7) + "," + ubnLine.get(9));
                            ubnResult.getIncorrectExchangeInfo().add(qso.get());
                        } else {
                            System.out.println(qso.get().toString());
                            result.add(qso.get().toString());
                        }
                    }
                }
                // read next line
                if (line.equalsIgnoreCase("************************* Not In Log *************************")) {
                    System.out.println("************************* Not In Log *************************");
                    result.add("************************* Not In Log *************************");
                    currentReport = NIL;
                    line = reader.readLine();
                } else if (line.equalsIgnoreCase("*********************** Incorrect Call ***********************")) {
                    System.out.println("*********************** Incorrect Call ***********************");
                    result.add("*********************** Incorrect Call ***********************");
                    currentReport = INCORRECT_CALL;

                } else if (line.equalsIgnoreCase("*************** Incorrect Exchange Information ***************")) {
                    System.out.println("*************** Incorrect Exchange Information ***************");
                    result.add("*************** Incorrect Exchange Information ***************");
                    currentReport = INCORRECT_EXCHANGE_INFO;

                } else if (line.equalsIgnoreCase("******************* Band Change Violations *******************")) {
                    System.out.println("******************* Band Change Violations *******************");
                    result.add("******************* Band Change Violations *******************");
                    currentReport = BAND_CHANGE_VIOLATION;

                } else if (line.equalsIgnoreCase("********* Unique Calls Receiving Credit (not removed)*********")) {
                    System.out.println("********* Unique Calls Receiving Credit (not removed)*********");
                    result.add("********* Unique Calls Receiving Credit (not removed)*********");
                    currentReport = BAND_CHANGE_VIOLATION;


                } else if (line.equalsIgnoreCase("********************** Lost Multipliers **********************")) {
                    System.out.println("********************** Lost Multipliers **********************");
                    currentReport = LOST_MULTI;

                } else if (line.equalsIgnoreCase("*******************  Multipliers by Band  ********************")) {
                    currentReport = 0;
                } else if (line.equalsIgnoreCase("************************ Multipliers *************************")) {
                    currentReport = 0;
                }
                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Long> totalByOperator = qsos.stream()
                .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        ubnResult.setTotalByOperator(totalByOperator);
        System.out.println("Total per operator: " + totalByOperator);

       Map<String, Long> incorrectCallPerOperator = ubnResult.getIncorrectCall().stream()
            .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        System.out.println("Incorrect call per operator: " + incorrectCallPerOperator);

        Map<String, Long> exchangePerOperator = ubnResult.getIncorrectExchangeInfo().stream()
            .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        System.out.println("Bad Exchange per operator: " + exchangePerOperator);

        Map<String, Double> errorPercentagePerOperator = new LinkedHashMap<>();

        for (String operator : totalByOperator.keySet()) {
            long total = totalByOperator.getOrDefault(operator, 0L);
            long invalidCalls = incorrectCallPerOperator.getOrDefault(operator, 0L);
            long badExchanges = exchangePerOperator.getOrDefault(operator, 0L);
            long notInLog = ubnResult.getNotInLog().stream()
                .filter(qso -> qso.getOperator().equalsIgnoreCase(operator))
                .count();
            long totalErrors = invalidCalls + badExchanges + notInLog;
            double percentage = total > 0 ? (totalErrors * 100.0) / total : 0.0;
            errorPercentagePerOperator.put(operator, percentage);
            OperatorErrorStats operatorErrorStats = new OperatorErrorStats(
                    operator, total, invalidCalls, badExchanges, notInLog, totalErrors, percentage
            );
            ubnResult.getOperatorErrorStats().add(operatorErrorStats);
        }

        System.out.println("Invalid Calls per operator chart:");
        for (Map.Entry<String, Long> entry : incorrectCallPerOperator.entrySet()) {
            String operator = entry.getKey();
            long count = entry.getValue();
            int barLength = (int) (count); // adjust scale if needed
            String bar = "#".repeat(barLength);
            System.out.printf("%-10s | %-5d | %s%n", operator, count, bar);
        }

        System.out.println("Bad Exchange per operator chart:");
        for (Map.Entry<String, Long> entry : exchangePerOperator.entrySet()) {
            String operator = entry.getKey();
            long count = entry.getValue();
            int barLength = (int) (count); // adjust scale if needed
            String bar = "#".repeat(barLength);
            System.out.printf("%-10s | %-5d | %s%n", operator, count, bar);
        }

        System.out.println("QSOs per operator chart:");
        for (Map.Entry<String, Long> entry : totalByOperator.entrySet()) {
            String operator = entry.getKey();
            long total = entry.getValue();
            int barLength = (int) (total / 2); // adjust scale as needed
            String bar = "#".repeat(barLength);
            System.out.printf("%-10s | %-5d | %s%n", operator, total, bar);
        }
        System.out.println("Error per operator chart:");
        for (Map.Entry<String, Double> entry : errorPercentagePerOperator.entrySet()) {
            String operator = entry.getKey();
            double percent = entry.getValue();
            int barLength = (int) (percent / 2); // scale for visibility
            String bar = "#".repeat(barLength);
            System.out.printf("%-10s | %-5.2f%% | %s%n", operator, percent, bar);
        }

        System.out.println("Error percentage per operator: " + errorPercentagePerOperator);
        return ubnResult;
    }
}

