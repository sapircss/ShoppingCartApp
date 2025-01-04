package com.example.shoppingcartapp.Models;

import androidx.annotation.NonNull;

public class ItemsModel {

    private String name;
    private int image;
    private  int id;

    public ItemsModel(String name, int image, int id) {
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Override toString for debugging
    @Override
    public String toString()
    {
        return "ItemsModel{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", id=" + id +
                '}';
    }


}
