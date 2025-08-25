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
package org.lw5hr.model;

/**
 *
 * @author diego
 */
public class CsvLine {
    private String line;
   
    public CsvLine (String qso) {
        line = qso;
    }
    
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
    
    
}
