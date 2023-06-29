package com.hanindya.ag5s.Model;

import java.util.HashMap;

public class Order {
    String customerName,customerType,orderType,createdByUser,createdDateTime,createdDate,orderStatus,cancelReason;
    double subtotalPrice,nominalPayment,change;
    int subtotalItem;
    HashMap<String,OrderItem>orderItem;

    public Order() {
    }

    public Order(String customerName, String customerType, String orderType, String createdByUser, String createdDateTime, String createdDate, String orderStatus, String cancelReason, double subtotalPrice, double nominalPayment, double change, int subtotalItem, HashMap<String, OrderItem> orderItem) {
        this.customerName = customerName;
        this.customerType = customerType;
        this.orderType = orderType;
        this.createdByUser = createdByUser;
        this.createdDateTime = createdDateTime;
        this.createdDate = createdDate;
        this.orderStatus = orderStatus;
        this.cancelReason = cancelReason;
        this.subtotalPrice = subtotalPrice;
        this.nominalPayment = nominalPayment;
        this.change = change;
        this.subtotalItem = subtotalItem;
        this.orderItem = orderItem;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public double getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(double subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public double getNominalPayment() {
        return nominalPayment;
    }

    public void setNominalPayment(double nominalPayment) {
        this.nominalPayment = nominalPayment;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public int getSubtotalItem() {
        return subtotalItem;
    }

    public void setSubtotalItem(int subtotalItem) {
        this.subtotalItem = subtotalItem;
    }

    public HashMap<String, OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(HashMap<String, OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
}
