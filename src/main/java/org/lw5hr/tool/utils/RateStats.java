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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class RateStats {
    public static class BestRate {
        public String operator;
        public int bestHourCount;

        public BestRate(String operator, int bestHourCount) {
            this.operator = operator;
            this.bestHourCount = bestHourCount;
        }
    }

    // Calculates the best clock hour QSO rate for each operator
    public List<BestRate> bestHourlyRateByOperator(List<Qso> qsos) {
        Map<String, List<Qso>> byOp = qsos.stream()
                .collect(Collectors.groupingBy(Qso::getOperator));
        List<BestRate> result = new ArrayList<>();

        for (Map.Entry<String, List<Qso>> entry : byOp.entrySet()) {
            Map<LocalDateTime, Long> hourCounts = entry.getValue().stream()
                .collect(Collectors.groupingBy(
                    qso -> qso.getDate().atTime(qso.getTime()).truncatedTo(ChronoUnit.HOURS),
                    Collectors.counting()
                ));
            long max = hourCounts.values().stream().mapToLong(Long::longValue).max().orElse(0);
            result.add(new BestRate(entry.getKey(), (int) max));
        }
        return result;
    }
}