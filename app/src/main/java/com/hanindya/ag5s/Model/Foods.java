package com.hanindya.ag5s.Model;

public class Foods {
    private String foodName,foodCategory;
    double foodPrice;
    int isEnable;

    public Foods() {
    }

    public Foods(String foodName, String foodCategory, double foodPrice, int isEnable) {
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.foodPrice = foodPrice;
        this.isEnable = isEnable;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }
}
