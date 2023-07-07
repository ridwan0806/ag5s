package com.hanindya.ag5s.Model;

public class Cost {
    String name,date,urlAttachment,createdBy,createdDateTime,notes;
    double subtotal;

    public Cost() {
    }

    public Cost(String name, String date, String urlAttachment, String createdBy, String createdDateTime, String notes, double subtotal) {
        this.name = name;
        this.date = date;
        this.urlAttachment = urlAttachment;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
        this.notes = notes;
        this.subtotal = subtotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
