package com.example.mydiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    ImageButton buttonAdd;
    private RecyclerView recyclerView;
    private ResycleAdapter adapter;

    DatabaseHelper DatabaseHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = (ImageButton)findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);


        DatabaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);

        SQLiteDatabase db = DatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String[] projection = {
                DBContract.DBEntry._ID,
                DBContract.DBEntry.COLUMN_NAME_DATE,
                DBContract.DBEntry.COLUMN_NAME_EMO,
                DBContract.DBEntry.COLUMN_NAME_DESCR,
                DBContract.DBEntry.COLUMN_NAME_IMG
        };
        Cursor cursor = db.query(
                DBContract.DBEntry.TABLE_TEXT,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        // Создаем адаптер для RecyclerView
        adapter = new ResycleAdapter(cursor, getApplicationContext());


        // Устанавливаем адаптер для RecyclerView
        recyclerView.setAdapter(adapter);

        // Устанавливаем линейный менеджер для RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public void onClick(View v) {


        int but = v.getId();

        if (but == R.id.buttonAdd){
            Intent intent = new Intent(getApplicationContext(), AddActivity.class);
            startActivity(intent);
            finish();
        }


        DatabaseHelper.close();
    }
}