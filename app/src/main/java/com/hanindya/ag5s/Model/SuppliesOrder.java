package com.hanindya.ag5s.Model;

import java.util.HashMap;

public class SuppliesOrder {
    private String date,urlAttachment,notes,createdBy,createdDateTime;
    double subtotal;
    HashMap<String,SuppliesOrderItem>items;

    public SuppliesOrder() {
    }

    public SuppliesOrder(String date, String urlAttachment, String notes, String createdBy, String createdDateTime, double subtotal, HashMap<String, SuppliesOrderItem> items) {
        this.date = date;
        this.urlAttachment = urlAttachment;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
        this.subtotal = subtotal;
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlAttachment() {
        return urlAttachment;
    }

    public void setUrlAttachment(String urlAttachment) {
        this.urlAttachment = urlAttachment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public HashMap<String, SuppliesOrderItem> getItems() {
        return items;
    }

    public void setItems(HashMap<String, SuppliesOrderItem> items) {
        this.items = items;
    }
}
