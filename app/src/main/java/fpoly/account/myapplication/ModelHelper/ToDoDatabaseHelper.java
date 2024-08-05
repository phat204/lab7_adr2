package fpoly.account.myapplication.ModelHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fpoly.account.myapplication.models.ToDo;

public class ToDoDatabaseHelper extends SQLiteOpenHelper {
    public ToDoDatabaseHelper(Context mContext) {
        super(mContext, "ToDoDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE TODO(\n" +
                "\tid integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    title text,\n" +
                "    content text,\n" +
                "    date text,\n" +
                "    type text,\n" +
                "    status text\n" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TODO");
        onCreate(db);
    }
}
