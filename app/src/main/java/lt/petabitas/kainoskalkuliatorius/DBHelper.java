package lt.petabitas.kainoskalkuliatorius;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 8;

    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_PREKES = "CREATE TABLE " + Prekes.TABLE + "("
                + Prekes.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Prekes.KEY_pavadinimas + " TEXT, "
                + Prekes.KEY_rusis + " TEXT, "
                + Prekes.KEY_bpvm + " TEXT, "
                + Prekes.KEY_kaina + " TEXT )";

        db.execSQL(CREATE_TABLE_PREKES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Prekes.TABLE);
        // Create tables again
        onCreate(db);

    }

    //Get Row Count
    public int getCount() {
        String countQuery = "SELECT * FROM " + Prekes.TABLE;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    //Delete Query
    public void removeFav(int id) {
        String countQuery = "DELETE FROM " + Prekes.TABLE + " where " + Prekes.KEY_ID + "= " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(countQuery);
    }

    //Insert Value
    public void adddata(String pavad, String rus, String bp, String k) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prekes.KEY_pavadinimas, pavad);
        values.put(Prekes.KEY_rusis, rus);
        values.put(Prekes.KEY_bpvm, bp);
        values.put(Prekes.KEY_kaina, k);
        db.insert(Prekes.TABLE, null, values);
        db.close();
    }

    //Update Value
    public void updatedata(int id, String pavad, String rus, String bp, String k) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prekes.KEY_pavadinimas, pavad);
        values.put(Prekes.KEY_rusis, rus);
        values.put(Prekes.KEY_bpvm, bp);
        values.put(Prekes.KEY_kaina, k);
        db.update(Prekes.TABLE, values, Prekes.KEY_ID + "=" + id, null);
        db.close();
    }

    //Get FavList
    public List<FavoriteListItem> getFavList() {
        String selectQuery = "SELECT  * FROM " + Prekes.TABLE + " ORDER BY " + Prekes.KEY_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<FavoriteListItem> favList = new ArrayList<FavoriteListItem>();
        if (cursor.moveToFirst()) {
            do {
                FavoriteListItem list = new FavoriteListItem();
                list.setId(cursor.getInt(cursor.getColumnIndex(Prekes.KEY_ID)));
                list.setPavadinimas(cursor.getString(cursor.getColumnIndex(Prekes.KEY_pavadinimas)));
                list.setRusis(cursor.getString(cursor.getColumnIndex(Prekes.KEY_rusis)));
                list.setBePVM(cursor.getString(cursor.getColumnIndex(Prekes.KEY_bpvm)));
                list.setKaina(cursor.getString(cursor.getColumnIndex(Prekes.KEY_kaina)));
                favList.add(list);
            } while (cursor.moveToNext());
        }
        return favList;
    }
}
