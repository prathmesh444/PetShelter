package com.example.petshelter;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshelter.data.PetContract;
import com.example.petshelter.data.PetDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PetCursorAdapter pAdapter;
    PetDB mDbHelper = new PetDB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView displayView = (ListView) findViewById(R.id.text_view_pet);
        View empty_view = findViewById(R.id.empty_view);
        displayView.setEmptyView(empty_view);
        pAdapter = new PetCursorAdapter(this,null,0);
        displayView.setAdapter(pAdapter);

        getLoaderManager().initLoader(0,null,this);

        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(CatalogActivity.this,EditorActivity.class);

                Uri currUri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,id);
                i.setData(currUri);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                Uri u = insertDB();
                if(u != null)
                    Toast.makeText(this,"New Pet Added Succesfully",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"No Pet Added",Toast.LENGTH_SHORT).show();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                getContentResolver().delete(PetContract.PetEntry.CONTENT_URI,null,null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri insertDB() {

        // Create and/or open a database to read from it
        ContentValues v = new ContentValues();
        v.put(PetContract.PetEntry.COLUMN_PET_NAME, "Tom");
        v.put(PetContract.PetEntry.COLUMN_PET_BREED, "local");
        v.put(PetContract.PetEntry.COLUMN_PET_GENDER, 1);
        v.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 56);

        Uri U = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, v);
        return U;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_WEIGHT,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER};

        CursorLoader c = new CursorLoader(this,PetContract.PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        return c;
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        pAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        pAdapter.swapCursor(null);
    }
}
