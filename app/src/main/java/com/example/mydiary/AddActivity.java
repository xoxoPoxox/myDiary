package com.example.mydiary;

import android.content.ContentValues;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class AddActivity extends AppCompatActivity {
    String[] emotions = { "Отлично", "Хорошо", "Нормально", "Плохо", "Ужасно"};
    ImageButton buttonOK, buttonNO, buttonPic;
    EditText ET_desc;
    ImageView ET_icon;
    String emo ="", img ="";
    private static final int PICK_IMAGE_REQUEST = 1;
    DatabaseHelper DatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        Spinner spinner = findViewById(R.id.spinner);
        buttonOK = findViewById(R.id.buttonOK);
        buttonNO = findViewById(R.id.buttonNO);
        buttonPic = findViewById(R.id.buttonPic);
        ET_icon = findViewById(R.id.ET_icon);
        ET_desc = (EditText)findViewById(R.id.ET_desc);
        TextView selection = findViewById(R.id.selection);
        DatabaseHelper = new DatabaseHelper(this);
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
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                String input_date = dateText;
                String input_emo = emo;
                String input_img = img;
                String input_descr = ET_desc.getText().toString();
                SQLiteDatabase db = DatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DBContract.DBEntry.COLUMN_NAME_DATE, input_date);
                values.put(DBContract.DBEntry.COLUMN_NAME_EMO, input_emo);
                values.put(DBContract.DBEntry.COLUMN_NAME_DESCR, input_descr);
                values.put(DBContract.DBEntry.COLUMN_NAME_IMG, input_img);
                db.insert(DBContract.DBEntry.TABLE_TEXT, null, values);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск галереи для выбора изображения
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Получение выбранного изображения
            Uri imageUri = data.getData();
            img = imageUri.toString();
            try {
                // Преобразование URI в Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Установка изображения в ImageView
                ET_icon.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}