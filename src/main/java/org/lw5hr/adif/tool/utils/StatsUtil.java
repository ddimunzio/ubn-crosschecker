package org.lw5hr.adif.tool.utils;

import org.lw5hr.adif.charts.BarChartHelper;
import org.lw5hr.adif.model.Qso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsUtil {

    public Map<LocalDate, List<BarChartHelper>> getRatesByHour(List<Qso> qsos) {
        final Map<LocalDate, Map<Integer, List<Qso>>> grouped = qsos.stream()
                .collect(Collectors.groupingBy(x -> x.getDate(), Collectors.groupingBy(a -> a.getTime().getHour())));

        List<BarChartHelper> topRatesHelper = new ArrayList<>();
        grouped.forEach((g, i) -> {
            i.forEach((q, z) -> {
                BarChartHelper trh = new BarChartHelper();
                    trh.setColKey(q.toString());
                    trh.setValue(z.size());
                    trh.setRowKey(q.toString());
                    trh.setDate(g);
                    topRatesHelper.add(trh);
            });
        });
        return topRatesHelper.stream()
                .collect(Collectors.groupingBy(BarChartHelper::date));
    }

    public void totalQsoByHourAndOperator(List<Qso> qsos) {
        Map<Object, Map<Object, Map<Object, List<Qso>>>> totalByHour = qsos.stream()
                .collect(Collectors.groupingBy(q -> q.getDate(),Collectors.groupingBy(q -> q.getTime().getHour(), Collectors.groupingBy(q -> q.getOperator()))));
        totalByHour.forEach((day, hourOperatorList) -> {
            System.out.print(System.getProperty("line.separator"));
            System.out.print("Day "+ day);
            System.out.print(System.getProperty("line.separator"));
            hourOperatorList.forEach((Hour, operatorsQso) -> {
                System.out.print(" HOUR "+ Hour);
                System.out.print(System.getProperty("line.separator"));
                operatorsQso.forEach((operator, qso) -> {
                    System.out.print(" OPERATOR "+ operator + " = " + qso.size());
                    System.out.print(System.getProperty("line.separator"));
                });
            });
            System.out.print(System.getProperty("line.separator"));
        });
    }


    public void groupByMinutes(List<Qso> qsos) {
        final Map<Integer, IntSummaryStatistics> a = qsos.stream()
                .collect(Collectors.groupingBy(q -> q.getTime().getMinute(), Collectors.summarizingInt(x -> 1)));
    }
}


