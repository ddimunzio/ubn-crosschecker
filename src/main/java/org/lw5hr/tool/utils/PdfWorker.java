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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.lw5hr.model.CtyDat;
import org.lw5hr.model.Qso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PdfWorker {


    public PdfWorker() throws IOException {
    }

    public void fillForm(HashMap<Qso, CtyDat> list) throws IOException {
        String rootPath = new java.io.File("").getAbsolutePath() + "/lista-de-tarjetas-QSL-QSL-card-list.pdf";
        PDDocument pdfDocument = PDDocument.load(new File(rootPath));
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        if (acroForm != null) {
            while (acroForm.getFieldIterator().hasNext()) {
                System.out.println();
            }
        }
    }
}
