package com.progmeleon.mycafe.model;

import java.io.Serializable;
import java.time.LocalDate;

public class SalesRecord implements Serializable {
    // section SalesRecord
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private double totalSales;

    public SalesRecord(LocalDate date, double totalSales) {
        this.date = date;
        this.totalSales = totalSales;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTotalSales() {
        return totalSales;
    }
}

