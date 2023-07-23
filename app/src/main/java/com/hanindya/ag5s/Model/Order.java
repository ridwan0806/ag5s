package com.hanindya.ag5s.Model;

import java.util.HashMap;

public class Order {
    String customerName,customerType,orderType,createdByUser,createdDateTime,createdDate,orderStatus,cancelReason,paymentMethod,completeDateTime,completeBy,cancelDateTime,cancelBy;
    double totalBill,paymentNominal,change;
    int totalItem;
    HashMap<String,OrderItem>orderItem;

    public Order() {
    }

    public Order(String customerName, String customerType, String orderType, String createdByUser, String createdDateTime, String createdDate, String orderStatus, String cancelReason, String paymentMethod, String completeDateTime, String completeBy, String cancelDateTime, String cancelBy, double totalBill, double paymentNominal, double change, int totalItem, HashMap<String, OrderItem> orderItem) {
        this.customerName = customerName;
        this.customerType = customerType;
        this.orderType = orderType;
        this.createdByUser = createdByUser;
        this.createdDateTime = createdDateTime;
        this.createdDate = createdDate;
        this.orderStatus = orderStatus;
        this.cancelReason = cancelReason;
        this.paymentMethod = paymentMethod;
        this.completeDateTime = completeDateTime;
        this.completeBy = completeBy;
        this.cancelDateTime = cancelDateTime;
        this.cancelBy = cancelBy;
        this.totalBill = totalBill;
        this.paymentNominal = paymentNominal;
        this.change = change;
        this.totalItem = totalItem;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCompleteDateTime() {
        return completeDateTime;
    }

    public void setCompleteDateTime(String completeDateTime) {
        this.completeDateTime = completeDateTime;
    }

    public String getCompleteBy() {
        return completeBy;
    }

    public void setCompleteBy(String completeBy) {
        this.completeBy = completeBy;
    }

    public String getCancelDateTime() {
        return cancelDateTime;
    }

    public void setCancelDateTime(String cancelDateTime) {
        this.cancelDateTime = cancelDateTime;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public double getPaymentNominal() {
        return paymentNominal;
    }

    public void setPaymentNominal(double paymentNominal) {
        this.paymentNominal = paymentNominal;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public HashMap<String, OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(HashMap<String, OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
}
