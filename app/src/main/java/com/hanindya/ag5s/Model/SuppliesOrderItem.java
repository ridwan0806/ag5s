package com.hanindya.ag5s.Model;

public class SuppliesOrderItem {
    private String name,category,notes,qty,units;
    double price,subtotal;

    public SuppliesOrderItem() {
    }

    public SuppliesOrderItem(String name, String category, String notes, String qty, String units, double price, double subtotal) {
        this.name = name;
        this.category = category;
        this.notes = notes;
        this.qty = qty;
        this.units = units;
        this.price = price;
        this.subtotal = subtotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
