package com.sked.razaform.database;

/**
 * Created by manish on 10/31/2015.
 */
public class Table {
    public static class User {
        public static final String TABLE_NAME = "credential";
        public static final String ID = "id",
                USERNAME = "username",
                MOBILENO = "mobileno",
                WALLPAPER = "wall",
                RINGTONE = "ringtone",
                IMAGE = "image";

        public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USERNAME + " TEXT ,"
                + MOBILENO + " TEXT ,"
                + WALLPAPER + " TEXT ,"
                + RINGTONE + " INTEGER ,"
                + IMAGE + " TEXT )";
    }

    public static class ChatData {
        public static final String TABLE_NAME = "chat_repository";
        public static final String _ID = "_id",
                USER_ID = "user_id",
                MESSAGE = "message",
                FILE_URI = "file_uri",
                TYPE = "type",
                TIME = "time";
        public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " INTEGER ,"
                + MESSAGE + " TEXT ,"
                + FILE_URI + " TEXT ,"
                + TYPE + " INTEGER DEFAULT 1 ,"
                + TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP )";
    }
}
