package com.example.mydiary;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

public class ResycleAdapter extends RecyclerView.Adapter<ResycleAdapter.ViewHolder> {

    public Context mContext;
    private Cursor cursor;
    DatabaseHelper DatabaseHelper;

    public ResycleAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.mContext = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Создаем представление для элемента списка
        View view = inflater.inflate(R.layout.item_list, parent, false);

        // Возвращаем новый экземпляр ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Перемещаем курсор на указанную позицию
        cursor.moveToPosition(position);

        // Получаем данные из курсора

        @SuppressLint("Range") Integer value_id = cursor.getInt(cursor.getColumnIndex(DBContract.DBEntry._ID));
        @SuppressLint("Range") String value_date = cursor.getString(cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_DATE));
        @SuppressLint("Range") String value_emotion = cursor.getString(cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_EMO));
        @SuppressLint("Range") String value_descr = cursor.getString(cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_DESCR));
        @SuppressLint("Range") String value_img = cursor.getString(cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_IMG));
        // Устанавливаем данные в представление
        holder.textDate.setText(value_date);
        holder.textEmotion.setText(value_emotion);
        holder.textDescr.setText(value_descr);

        // Устанавливаем слушатель нажатия для элемента списка
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переходим на новое активити и передаем данные
                Intent intent = new Intent(mContext, UpdateActivity.class);
                intent.putExtra("id", value_id);

                //cursor.close();

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textDate;
        private TextView textEmotion;
        private TextView textDescr;

        public ViewHolder(View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.textDate);
            textEmotion = itemView.findViewById(R.id.textEmotion);
            textDescr = itemView.findViewById(R.id.textDescr);
        }
    }
}

