package com.example.petshelter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

public class PetDB extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Shelter.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_TABLE = "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + " ("
            + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
            + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
            + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
            + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+PetContract.PetEntry.TABLE_NAME;

    public PetDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + " ("
                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {

    }
}
