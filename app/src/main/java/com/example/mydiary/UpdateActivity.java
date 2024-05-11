package com.example.mydiary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    String[] emotions = { "Не выбрано", "Отлично", "Хорошо", "Нормально", "Плохо", "Ужасно"};
    ImageButton buttonOK, buttonNO, buttonPic, buttonCanc;
    EditText ET_desc;
    ImageView ET_icon;
    String emo ="", img ="";
    private static final int PICK_IMAGE_REQUEST = 1;
    DatabaseHelper DatabaseHelper;
    public String image = "";


    @SuppressLint("Range")
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
        DatabaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase();
        Intent intent = getIntent();
        //String image = "";
        //String descr = intent.getStringExtra("descr");
        Integer id = intent.getIntExtra("id", 0);
        //Integer position = intent.getIntExtra("position", 0);

        //ET_desc.setText(id.toString());
        String[] projection = {
                DBContract.DBEntry.COLUMN_NAME_EMO,
                DBContract.DBEntry.COLUMN_NAME_DESCR,
                DBContract.DBEntry.COLUMN_NAME_IMG
        };

        String sel = DBContract.DBEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                DBContract.DBEntry.TABLE_TEXT,
                projection,
                sel,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.moveToFirst();


        @SuppressLint("Range") String emotion = cursor.getString(cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_EMO));
        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_DESCR));
        img = cursor.getString(cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_IMG));
        // Закрываем курсор
        cursor.close();
        ET_desc.setText(description);

        if (!img.isEmpty()) {
            Uri imageUri = Uri.parse(img);


            ET_icon.setImageURI(imageUri);
        }

        selection.setText(emotion);
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
                selection.setText(emotion);
                if (item!= "Не выбрано") {
                    emo = item;
                }else{
                    emo = emotion;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_descr = ET_desc.getText().toString();
                String input_emo = emo;
                if (image != img){
                    img = image;
                }
                String input_img = img;
                ContentValues values = new ContentValues();
                values.put(DBContract.DBEntry.COLUMN_NAME_EMO, input_emo);
                values.put(DBContract.DBEntry.COLUMN_NAME_DESCR, input_descr);
                values.put(DBContract.DBEntry.COLUMN_NAME_IMG, input_img);
                db.update(DBContract.DBEntry.TABLE_TEXT, values, DBContract.DBEntry._ID + "=" + id, null);
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
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
            }
        });

        buttonCanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(DBContract.DBEntry.TABLE_TEXT, "_ID = ?", new String[]{String.valueOf(id)});
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Получение выбранного изображения
            Uri imageUri = data.getData();

            try {
                // Преобразование URI в Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);


                // Установка изображения в ImageView
                ET_icon.setImageBitmap(bitmap);
                image = imageUri.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}