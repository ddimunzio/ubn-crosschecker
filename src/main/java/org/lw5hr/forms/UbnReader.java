package org.lw5hr.forms;

import org.lw5hr.model.Qso;
import org.lw5hr.tool.utils.ADIFParser;
import org.lw5hr.tool.utils.ADIFReader;
import org.lw5hr.tool.utils.UBNReader;

import java.io.File;

public class UbnReader  {
    public static void main(String[] args) throws Exception {
        connect();
    }

    public static void connect() throws Exception {
        ADIFReader reader = new ADIFReader("C:\\Users\\lw5hr\\proyects\\Ubn\\LP1H-WPXSSB2025.adi");
        UBNReader ubnReader = new UBNReader();
        File ubn = new File("C:\\Users\\lw5hr\\proyects\\Ubn\\ubn.txt");
        ubnReader.readUbnFile(ubn, reader.read());




    }
}