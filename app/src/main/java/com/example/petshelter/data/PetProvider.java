package com.example.petshelter.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    PetDB mDbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_Pet, 100);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_Pet + "/#", 101);
    }

    @Override
    public boolean onCreate() {
       mDbHelper = new PetDB(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 100:
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case 101:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case 100:
                if(contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME) == null)
                    throw new IllegalArgumentException("Pet requires name");
                if(contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT) < 0)
                    contentValues.putNull(PetContract.PetEntry.COLUMN_PET_WEIGHT);
                if(contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_GENDER) == null)
                    throw new IllegalArgumentException("Pet Gender cannot be null");
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {
        SQLiteDatabase S = mDbHelper.getWritableDatabase();

        long id = S.insert(PetContract.PetEntry.TABLE_NAME,null,values);


        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }
    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase S = mDbHelper.getWritableDatabase();
        selection = PetContract.PetEntry._ID + "=?";
        selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
        int id = S.update(PetContract.PetEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        if (id != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return id;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int i;
        switch (match) {
            case 100:
                // Delete all rows that match the selection and selection args
                i = database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case 101:
                // Delete a single row given by the ID in the URI
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                i = database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (i != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return i;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
        public String getType(Uri uri) {
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case 100:
                    return PetContract.PetEntry.CONTENT_LIST_TYPE;
                case 101:
                    return PetContract.PetEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
            }
    }
}
