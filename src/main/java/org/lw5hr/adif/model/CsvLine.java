/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.lw5hr.adif.model;

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
