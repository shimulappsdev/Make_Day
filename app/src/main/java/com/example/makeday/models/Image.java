package com.example.makeday.models;

public class Image {
    String note_Id;
    String image_Id;
    String note_Image;

    public Image() {
    }

    public Image(String note_Id, String image_Id, String note_Image) {
        this.note_Id = note_Id;
        this.image_Id = image_Id;
        this.note_Image = note_Image;
    }

    public String getNote_Id() {
        return note_Id;
    }

    public void setNote_Id(String note_Id) {
        this.note_Id = note_Id;
    }

    public String getImage_Id() {
        return image_Id;
    }

    public void setImage_Id(String image_Id) {
        this.image_Id = image_Id;
    }

    public String getNote_Image() {
        return note_Image;
    }

    public void setNote_Image(String note_Image) {
        this.note_Image = note_Image;
    }
}
