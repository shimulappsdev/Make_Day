package com.example.makeday.models;

public class ListModel {

    String list_id;
    String list_title;
    String list_type;
    String list_date;

    public ListModel() {
    }

    public ListModel(String list_id, String list_title, String list_type, String list_date) {
        this.list_id = list_id;
        this.list_title = list_title;
        this.list_type = list_type;
        this.list_date = list_date;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getList_title() {
        return list_title;
    }

    public void setList_title(String list_title) {
        this.list_title = list_title;
    }

    public String getList_type() {
        return list_type;
    }

    public void setList_type(String list_type) {
        this.list_type = list_type;
    }

    public String getList_date() {
        return list_date;
    }

    public void setList_date(String list_date) {
        this.list_date = list_date;
    }
}
