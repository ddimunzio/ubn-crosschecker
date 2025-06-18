import org.junit.jupiter.api.Test;
import org.lw5hr.model.Qso;
import org.lw5hr.model.UbnResult;
import org.lw5hr.tool.utils.UBNReader;
import org.lw5hr.model.OperatorErrorStats;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UBNReaderTest {
    @Test
    void testErrorPercentagePerOperator() throws Exception {
        // Prepare a temporary UBN file with a known error (realistic format)
        File tempFile = File.createTempFile("test", ".ubn");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("*********************** Incorrect Call ***********************\n");
            writer.write("14222 PH 2025-03-29 0001 OP1    006   CALL1  001  correct CALLX");
        }

        // Prepare QSOs
        // Prepare QSOs
        Qso qso = new Qso();
        qso.setOperator("OP1");
        qso.setDate(LocalDate.of(2025, 3, 29)); // Match UBN line date
        qso.setTime(LocalTime.of(0, 1));        // Match UBN line time
        qso.setCall("CALL1");
        List<Qso> qsos = Arrays.asList(qso);

        // Run UBNReader
        UBNReader reader = new UBNReader();
        UbnResult result = reader.readUbnFile(tempFile, qsos);

        // Assert error percentage for OP1 is 100% (calculated manually)
        assertEquals(1, result.getOperatorErrorStats().size());
        OperatorErrorStats stats = result.getOperatorErrorStats().get(0);
        double errorPercentage = stats.getTotal() > 0 ? (stats.getTotalErrors() * 100.0) / stats.getTotal() : 0.0;
        assertEquals(100.0, errorPercentage, 0.01);

        tempFile.delete();
    }
}