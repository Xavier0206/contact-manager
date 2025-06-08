package com.example.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContactController {
    public static void exportToPDF(DefaultTableModel model) {
        try {
            String fileName = "contactos_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            
            document.add(new Paragraph("Lista de Contactos"));
            document.add(new Paragraph(" "));
            
            PdfPTable pdfTable = new PdfPTable(model.getColumnCount());
            
            // Encabezados
            for (int i = 0; i < model.getColumnCount(); i++) {
                pdfTable.addCell(model.getColumnName(i));
            }
            
            // Datos
            for (int rows = 0; rows < model.getRowCount(); rows++) {
                for (int cols = 0; cols < model.getColumnCount(); cols++) {
                    pdfTable.addCell(model.getValueAt(rows, cols).toString());
                }
            }
            
            document.add(pdfTable);
            document.close();
            
            JOptionPane.showMessageDialog(
                null, 
                "PDF exportado: " + fileName, 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE
            );
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null, 
                "Error al exportar: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}