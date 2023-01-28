package com.example.makeday.models;

public class Item {

    String item_id;
    String list_id;
    String item_name;
    String item_unit;
    String item_quantity;
    String item_amount;
    String item_status;

    public Item() {
    }

    public Item(String item_id, String list_id, String item_name, String item_unit, String item_quantity, String item_amount, String item_status) {
        this.item_id = item_id;
        this.list_id = list_id;
        this.item_name = item_name;
        this.item_unit = item_unit;
        this.item_quantity = item_quantity;
        this.item_amount = item_amount;
        this.item_status = item_status;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(String item_amount) {
        this.item_amount = item_amount;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }
}
