package com.example.jbt.iebdappbykoby;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jbt on 14/11/2017.
 */

//creating data base
public class MySqlHelper extends SQLiteOpenHelper {
    public MySqlHelper(Context context) {
        super(context,"movie.db" ,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     String command = "CREATE TABLE "+
             DBconstants.TABLE_NAME+
             " (_id INTEGER " + " PRIMARY KEY AUTOINCREMENT , "
             + DBconstants.NAME_COLUMN + " TEXT , "
             + DBconstants.BODY_COLUMN + " TEXT, "
             + DBconstants.URL_COLUMN + " TEXT  ) ";
        sqLiteDatabase.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //removing data base
    public void removeRecord(String positionInString) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(DBconstants.TABLE_NAME, "_id = ?", new String[]{positionInString});
            Log.d("sql", "Deleted Record in position " + positionInString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
