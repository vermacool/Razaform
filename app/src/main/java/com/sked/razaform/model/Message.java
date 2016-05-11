package com.sked.razaform.model;

import android.net.Uri;

/**
 * Created by manish on 11/1/2015.
 */
public class Message {

    public static final int OUTGOING = 1;
    public static final int INCOMING = 0;
    private int id;
    private String text;
    private Uri fileUri;
    private int type;
    private String time;

    public Message(String text) {
        this.text = text;
    }

    public Message() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
