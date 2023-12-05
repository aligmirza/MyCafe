package com.progmeleon.mycafe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Deal {
    private static int lastId = 0;

    private int id;
    private List<Item> dealItems; // Assuming Item is a class representing individual items
    private String dealName;
    private double dealPrice;
    private String dealDescription;

    public Deal(@JsonProperty("dealName") String dealName, @JsonProperty("dealItems") List<Item> dealItems, @JsonProperty("dealPrice") double dealPrice, @JsonProperty("dealDescription") String dealDescription) {
        this.id = ++lastId;
        this.dealItems = dealItems;
        this.dealName = dealName;
        this.dealPrice = dealPrice;
        this.dealDescription = dealDescription;
    }

    public Deal(int dealId, String dealName, double dealPrice, String dealDescription) {
        this.id = dealId;
        this.dealItems = new ArrayList<>();
        this.dealName = dealName;
        this.dealPrice = dealPrice;
        this.dealDescription = dealDescription;
    }

    public int getId() {
        return id;
    }

    public List<Item> getDealItems() {
        return dealItems;
    }

    public String getDealName() {
        return dealName;
    }

    public double getDealPrice() {
        return dealPrice;
    }

    public String getDealDescription() {
        return dealDescription;
    }

    // Other getters and setters

    @Override
    public String toString() {
        return "Deal{" +
                "id=" + id +
                ", dealItems=" + dealItems +
                ", dealName='" + dealName + '\'' +
                ", dealPrice=" + dealPrice +
                ", dealDescription='" + dealDescription + '\'' +
                '}';
    }
}

