package com.example.mydiary;

import android.provider.BaseColumns;

public class DBContract {
    private DBContract() {}
    public static class DBEntry implements BaseColumns {
        public static final String TABLE_TEXT = "notes";
        public static final String _ID = "_ID";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_EMO = "emotion";
        public static final String COLUMN_NAME_DESCR = "description";
        public static final String COLUMN_NAME_IMG = "image";
    }

}
