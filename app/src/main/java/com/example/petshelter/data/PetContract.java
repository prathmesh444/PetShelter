package com.example.petshelter.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PetContract {

    private PetContract() {}
    public static final String CONTENT_AUTHORITY = "com.example.petshelter";
    public static final Uri BASE_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_Pet = "Pets";
    public static final class PetEntry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "Pet";
        public static final String COLUMN_PET_NAME = "Name";
        public static final String COLUMN_PET_WEIGHT = "Weight";
        public static final String COLUMN_PET_BREED = "Breed";
        public static final String COLUMN_PET_GENDER = "Gender";

        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKNOWN = 0;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,PATH_Pet);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Pet;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Pet;

    }
}
