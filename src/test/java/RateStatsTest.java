import org.junit.jupiter.api.Test;
import org.lw5hr.model.Qso;
import org.lw5hr.tool.utils.RateStats;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RateStatsTest {
    @Test
    void testBestHourlyRateByOperator_DifferentOperatorsSameHour() {
        // Both operators have QSOs in the same hour (10:00)
        Qso q1 = new Qso();
        q1.setOperator("op1");
        q1.setDate(LocalDate.of(2024, 6, 2));
        q1.setTime(LocalTime.of(10, 5));

        Qso q2 = new Qso();
        q2.setOperator("op1");
        q2.setDate(LocalDate.of(2024, 6, 2));
        q2.setTime(LocalTime.of(10, 15));

        Qso q3 = new Qso();
        q3.setOperator("op2");
        q3.setDate(LocalDate.of(2024, 6, 2));
        q3.setTime(LocalTime.of(10, 20));

        Qso q4 = new Qso();
        q4.setOperator("op2");
        q4.setDate(LocalDate.of(2024, 6, 2));
        q4.setTime(LocalTime.of(10, 45));

        List<Qso> qsos = Arrays.asList(q1, q2, q3, q4);

        RateStats stats = new RateStats();
        List<RateStats.BestRate> result = stats.bestHourlyRateByOperator(qsos);

        assertEquals(2, result.stream().filter(r -> r.operator.equals("op1")).findFirst().get().bestHourCount);
        assertEquals(2, result.stream().filter(r -> r.operator.equals("op2")).findFirst().get().bestHourCount);
    }
}