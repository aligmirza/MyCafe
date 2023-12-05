package com.progmeleon.mycafe;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.config.ReceiptPrinter;
import com.progmeleon.mycafe.controller .InventorySystem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.progmeleon.mycafe.controller.OrderHandler.placeOrder;

public class  Main  {
    // section main
    public static void main(String[] args) throws IOException {
        ConfigureExistingData.loadExistingData();
        new InventorySystem().start();
////        placeOrder();
//
////        String receiptContent = "Item 1: $10.00\nItem 2: $20.00\nTotal: $30.00";
////
////        ReceiptPrinter receiptPrinter = new ReceiptPrinter(receiptContent);
////        receiptPrinter.printReceipt();
//
    }


}
