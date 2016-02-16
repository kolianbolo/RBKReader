package ru.bolobanov.rbkreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import ru.bolobanov.rbkreader.Article;
import ru.bolobanov.rbkreader.Constants;

/**
 * Created by Bolobanov Nikolay on 09.12.15.
 */
public class RbkDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rbk.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    private static final String RSS_CREATE = "CREATE TABLE " + Constants.RSS_TABLE +
            " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.COLUMN_GUID +
            " TEXT NOT NULL, " + Constants.COLUMN_TITLE + " TEXT NOT NULL, " + Constants.COLUMN_LINK + " TEXT NOT NULL);";

    private static final String HTML_CREATE = "CREATE TABLE " + Constants.HTML_TABLE +
            " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.COLUMN_URL +
            " TEXT NOT NULL, " + Constants.COLUMN_DATA + " TEXT NOT NULL);";

    public RbkDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RSS_CREATE);
        db.execSQL(HTML_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveArticle(Article pArticle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_GUID, pArticle.mGuid);
        contentValues.put(Constants.COLUMN_TITLE, pArticle.mTitle);
        contentValues.put(Constants.COLUMN_LINK, pArticle.mLink);
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        database.insert(Constants.RSS_TABLE, null, contentValues);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public void deleteArticle(String pGuid) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        database.delete(Constants.RSS_TABLE, Constants.COLUMN_GUID + " like ?", new String[]{pGuid});
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public List<Article> getArticles() {
        List<Article> returned = new ArrayList<>();
        final SQLiteDatabase database = getReadableDatabase();
        final Cursor cursor = database.query(Constants.RSS_TABLE,
                new String[]{BaseColumns._ID, Constants.COLUMN_TITLE, Constants.COLUMN_LINK, Constants.COLUMN_GUID},
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Constants.COLUMN_GUID)),
                        cursor.getString(cursor.getColumnIndex(Constants.COLUMN_LINK)));
                returned.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returned;
    }

    public String getHtml(String pUrl) {
        String returned = null;
        final SQLiteDatabase database = getReadableDatabase();
        final Cursor cursor = database.query(Constants.HTML_TABLE,
                new String[]{Constants.COLUMN_DATA}, Constants.COLUMN_URL + " like ?", new String[]{pUrl}, null, null, null, null);
        if (cursor.moveToFirst()) {
            returned = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATA));
        }
        cursor.close();
        database.close();
        return returned;
    }

    public void saveHtml(String pUrl, String pHtml) {
        if (pHtml == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_URL, pUrl);
        contentValues.put(Constants.COLUMN_DATA, pHtml);
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        database.insert(Constants.HTML_TABLE, null, contentValues);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public void deleteHtmls() {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        database.delete(Constants.HTML_TABLE, null, null);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

}

