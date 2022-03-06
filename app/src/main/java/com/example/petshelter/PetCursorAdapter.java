package com.example.petshelter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.petshelter.data.PetContract;

import java.util.zip.Inflater;

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_items, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView t1 = view.findViewById(R.id.name);
        TextView t2 = view.findViewById(R.id.summary);
        int i1 = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int i2 = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
        String name = cursor.getString(i1);
        String breed = cursor.getString(i2);
        if(TextUtils.isEmpty(breed))
            breed = "Unknown Breed";
        t1.setText(name);
        t2.setText(breed);
    }
}
