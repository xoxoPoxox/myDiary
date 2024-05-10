package com.example.mydiary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {
    String[] emotions = { "Отлично", "Хорошо", "Нормально", "Плохо", "Ужасно"};
    ImageButton buttonOK, buttonNO, buttonPic, buttonCanc;
    EditText ET_desc;
    ImageView ET_icon;
    String emo ="", img ="";
    private static final int PICK_IMAGE_REQUEST = 1;
    DatabaseHelper DatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        Spinner spinner = findViewById(R.id.spinner);
        buttonOK = findViewById(R.id.buttonOK);
        buttonNO = findViewById(R.id.buttonNO);
        buttonPic = findViewById(R.id.buttonPic);
        buttonCanc = findViewById(R.id.buttonCanc);
        ET_icon = findViewById(R.id.ET_icon);
        ET_desc = (EditText)findViewById(R.id.ET_desc);
        TextView selection = findViewById(R.id.selection);

        Intent intent = getIntent();
        //Long id_text = intent.getLongExtra("id_text", 0);
        String date = intent.getStringExtra("date");
        String descr = intent.getStringExtra("descr");

        ET_desc.setText(descr);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, emotions);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                selection.setText(item);
                emo = item;
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
}