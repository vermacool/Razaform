package com.sked.razaform.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by manish on 10/21/2015.
 */
public class DataBase extends SQLiteOpenHelper {
    public static String DB = "RazaForm.db";
    public static int version = 1;

    public DataBase(Context context) {
        super(context, DB, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table.User.CREATE_TABLE);

        //Creating the new table
        db.execSQL(Table.ChatData.CREATE_TABLE);
    }
    /*public Cursor retriveData(int id) throws SQLException
    {
        this.
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   /* }
    public DataBase connect() throws SQLException {

    }*/

}}