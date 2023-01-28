package com.example.makeday.models;

import java.util.List;

public class Task {

    String task_id;
    String task_title;
    String task_category;
    String task_priority;
    String task_startTime;
    String task_endTime;
    String task_date;
    String task_location;
    String task_status;
    String Task_mode;
    String task_shortDes;

    public Task() {
    }

    public Task(String task_id, String task_title, String task_category, String task_priority, String task_startTime, String task_endTime, String task_date, String task_location, String task_status, String task_mode, String task_shortDes) {
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_category = task_category;
        this.task_priority = task_priority;
        this.task_startTime = task_startTime;
        this.task_endTime = task_endTime;
        this.task_date = task_date;
        this.task_location = task_location;
        this.task_status = task_status;
        Task_mode = task_mode;
        this.task_shortDes = task_shortDes;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_category() {
        return task_category;
    }

    public void setTask_category(String task_category) {
        this.task_category = task_category;
    }

    public String getTask_priority() {
        return task_priority;
    }

    public void setTask_priority(String task_priority) {
        this.task_priority = task_priority;
    }

    public String getTask_startTime() {
        return task_startTime;
    }

    public void setTask_startTime(String task_startTime) {
        this.task_startTime = task_startTime;
    }

    public String getTask_endTime() {
        return task_endTime;
    }

    public void setTask_endTime(String task_endTime) {
        this.task_endTime = task_endTime;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getTask_location() {
        return task_location;
    }

    public void setTask_location(String task_location) {
        this.task_location = task_location;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getTask_mode() {
        return Task_mode;
    }

    public void setTask_mode(String task_mode) {
        Task_mode = task_mode;
    }

    public String getTask_shortDes() {
        return task_shortDes;
    }

    public void setTask_shortDes(String task_shortDes) {
        this.task_shortDes = task_shortDes;
    }
}
