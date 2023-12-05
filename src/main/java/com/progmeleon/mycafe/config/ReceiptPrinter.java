package com.progmeleon.mycafe.config;

import java.awt.*;
import java.awt.print.*;

public class ReceiptPrinter implements Printable {
    private String receiptContent;

    public ReceiptPrinter(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        Font font = new Font("Courier New", Font.PLAIN, 12);
        graphics.setFont(font);

        String[] lines = receiptContent.split("\n");
        int y = 20; // Starting y-coordinate

        for (String line : lines) {
            graphics.drawString(line, 10, y);
            y += 15; // Adjust for the next line
        }

        return Printable.PAGE_EXISTS;
    }

    public void printReceipt() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        // Example receipt content
//
//    }
}
