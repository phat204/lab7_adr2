package fpoly.account.myapplication.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fpoly.account.myapplication.ModelHelper.ToDoDatabaseHelper;
import fpoly.account.myapplication.models.ToDo;

public class ToDoDAO {
    private SQLiteDatabase db;

    public ToDoDAO(Context mContext) {
        ToDoDatabaseHelper dbHelper = new ToDoDatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();
    }

    public long addToDo(ToDo toDo) {
        ContentValues values = new ContentValues();
        values.put("title", toDo.getTitle());
        values.put("content", toDo.getContent());
        values.put("date", toDo.getDate());
        values.put("type", toDo.getType());
        values.put("status", toDo.getStatus());
        return db.insert("TODO", null, values);
    }

    public List<ToDo> getAllData(){
        List<ToDo> toDoList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM TODO", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ToDo toDo = new ToDo();
                toDo.setId(cursor.getString(0));
                toDo.setTitle(cursor.getString(1));
                toDo.setContent(cursor.getString(2));
                toDo.setDate(cursor.getString(3));
                toDo.setType(cursor.getString(4));
                toDo.setStatus(cursor.getInt(5));
                toDoList.add(toDo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return toDoList;
    }

    public void updateStatusToDo(ToDo toDo) {
        ContentValues values = new ContentValues();
        values.put("status", toDo.getStatus());
        db.update("TODO", values, "id = ?", new String[]{String.valueOf(toDo.getId())});
    }

    public void updateToDo(ToDo toDo) {
        ContentValues values = new ContentValues();
        values.put("id", toDo.getId());
        values.put("title", toDo.getTitle());
        values.put("content", toDo.getContent());
        values.put("date", toDo.getDate());
        values.put("type", toDo.getType());
        values.put("status", toDo.getStatus());
        db.update("TODO", values, "id = ?", new String[]{String.valueOf(toDo.getId())});
    }

    public void deleteToDo(int id) {
        db.delete("TODO", "id = ?", new String[]{String.valueOf(id)});
    }
}
