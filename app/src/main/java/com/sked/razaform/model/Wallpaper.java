package com.sked.razaform.model;

/**
 * Created by manish on 11/1/2015.
 */
public class Wallpaper {
    private String name;
    private int imageId;

    public Wallpaper() {
         }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


    public Wallpaper(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;

    }

}
