package com.nhom1.tlulearningonline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.File;

public class VideoDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SavedVideos.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "videos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LESSON_ID = "lesson_id";
    public static final String COLUMN_LOCAL_URI = "local_uri";

    public VideoDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LESSON_ID + " TEXT UNIQUE, " +
                COLUMN_LOCAL_URI + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addVideo(String lessonId, String localUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LESSON_ID, lessonId);
        values.put(COLUMN_LOCAL_URI, localUri);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String getLocalVideoPath(String lessonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_LOCAL_URI},
                COLUMN_LESSON_ID + "=?", new String[]{lessonId},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCAL_URI));
            cursor.close();

            // Kiểm tra xem file có thực sự tồn tại không
            Uri uri = Uri.parse(path);
            if (uri.getPath() != null) {
                File file = new File(uri.getPath());
                if(file.exists()){
                    return path;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
}