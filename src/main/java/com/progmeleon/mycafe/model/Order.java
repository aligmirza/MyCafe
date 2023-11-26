package com.progmeleon.mycafe.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private LocalDateTime orderTime;
    private User user;
    private List<Item> items;
    private OrderStatus orderStatus;

    public Order(int orderId, User user, List<Item> items, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderTime = LocalDateTime.now();
        this.user = user;
        this.items = items;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderTime=" + orderTime +
                ", user=" + user +
                ", items=" + items +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
