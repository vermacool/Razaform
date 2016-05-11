package com.sked.razaform.model;

import android.net.Uri;

import java.net.URI;

/**
 * Created by manish on 11/1/2015.
 */
public class Contact {
    private int id;
    private Uri fileUri;
    private String name;
    private  String mobileNumber;

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Contact(int id, String mobileNumber, String name, Uri uri) {

        this.id = id;
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.fileUri=uri;
    }
}
