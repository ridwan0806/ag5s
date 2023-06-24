package com.hanindya.ag5s.Model;

public class OrderItem {
    private String Id,FoodId,FoodName;
    int Qty;
    double Price,Subtotal;

    public OrderItem() {
    }

    public OrderItem(String id, String foodId, String foodName, int qty, double price, double subtotal) {
        Id = id;
        FoodId = foodId;
        FoodName = foodName;
        Qty = qty;
        Price = price;
        Subtotal = subtotal;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(double subtotal) {
        Subtotal = subtotal;
    }
}
