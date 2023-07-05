package com.hanindya.ag5s.Model;

public class Menu {
    String Name,Category,CreatedBy,CreatedDateTime,EditedBy,EditedDateTime;
    double Price;

    public Menu() {
    }

    public Menu(String name, String category, String createdBy, String createdDateTime, String editedBy, String editedDateTime, double price) {
        Name = name;
        Category = category;
        CreatedBy = createdBy;
        CreatedDateTime = createdDateTime;
        EditedBy = editedBy;
        EditedDateTime = editedDateTime;
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }

    public String getEditedBy() {
        return EditedBy;
    }

    public void setEditedBy(String editedBy) {
        EditedBy = editedBy;
    }

    public String getEditedDateTime() {
        return EditedDateTime;
    }

    public void setEditedDateTime(String editedDateTime) {
        EditedDateTime = editedDateTime;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
