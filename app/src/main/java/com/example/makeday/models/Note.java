package com.example.makeday.models;

public class Note {

    String note_id;
    String note_title;
    String note_category;
    String note_time;
    String note_date;
    String note_body;

    public Note() {
    }

    public Note(String note_id, String note_title, String note_category, String note_time, String note_date, String note_body) {
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_category = note_category;
        this.note_time = note_time;
        this.note_date = note_date;
        this.note_body = note_body;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_category() {
        return note_category;
    }

    public void setNote_category(String note_category) {
        this.note_category = note_category;
    }

    public String getNote_time() {
        return note_time;
    }

    public void setNote_time(String note_time) {
        this.note_time = note_time;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public String getNote_body() {
        return note_body;
    }

    public void setNote_body(String note_body) {
        this.note_body = note_body;
    }
}
