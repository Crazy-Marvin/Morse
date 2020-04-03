package rocks.poopjournal.morse;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "morse";
    private static final int DATABASE_VERSION = 1;

    // table name
    private static final String TABLE_PHRASEBOOK = "phrasebook";


    private static final String KEY_ID = "id";
    private static final String TAG = "DBhandler";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_PHRASEBOOK = "CREATE TABLE IF NOT EXISTS "
                + TABLE_PHRASEBOOK + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "sentence" + " TEXT,"
                + "morse" + " TEXT);";
        db.execSQL(CREATE_NOTES_PHRASEBOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addPhrase(String text, String morse) {

        for (PhrasebookModel model : getAllPhrases()) {
            if (model.text.trim().equals(text.trim())) {
                deleteNote(model.id);
                Log.d("debug_star", "deleted note, now returning");
                return;
            }
        }

        Log.d("debug_star", "adding note, now returning");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sentence", text);
        values.put("morse", morse);

        db.insert(TABLE_PHRASEBOOK, null, values);
        db.close(); // Closing database connection
    }


    public void deleteNote(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHRASEBOOK, "id= " + id, null);
        db.close(); // Closing database connection

    }

    public ArrayList<PhrasebookModel> getAllPhrases() {
        String selectQuery = "SELECT * FROM " + TABLE_PHRASEBOOK;
        ArrayList<PhrasebookModel> mList = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                mList.add(new PhrasebookModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
                // Log.d(TAG, "title"+cursor.getString(3)+" notes="+cursor.getString(4));
            }
            db.close();
            return mList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
