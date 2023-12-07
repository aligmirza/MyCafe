package com.progmeleon.mycafe.config;

import java.awt.*;
import java.awt.print.*;

public class ReceiptPrinter implements Printable {

    private StringBuilder receipt;

    public ReceiptPrinter(StringBuilder receipt) {
        this.receipt = receipt;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        Font font = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(font);

        int lineHeight = g2d.getFontMetrics().getHeight();
        int x = 10; // starting X position
        int y = 10; // starting Y position

        // Print receipt details
        g2d.drawString(receipt.toString(), x, y);

        return PAGE_EXISTS;
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
}