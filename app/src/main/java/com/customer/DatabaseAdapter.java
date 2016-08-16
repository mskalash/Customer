package com.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by oleh on 10/17/15.
 */
public class DatabaseAdapter {

    SQLHelper helper;
    Context context;

    public DatabaseAdapter(Context context) {
        helper = new SQLHelper(context);
        this.context = context;
    }

    public void selectprofile(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLHelper.TABLE_NAME_CONTACTS + " WHERE " + SQLHelper.CONTACTS_ID + "=" + id, null);
        while (cursor.moveToNext()) {
            int lastIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_LAST);
            int nameIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_NAME);
            int descIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_DESCRIPTION);
            int iconIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_ICON);
            int fileIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_REC);
            int latIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_LAT);
            int longIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_LONGET);


            String filename = cursor.getString(fileIndex);
            String last = cursor.getString(lastIndex);
            String name = cursor.getString(nameIndex);
            String desc = cursor.getString(descIndex);
            String icon = cursor.getString(iconIndex);
            double lat = cursor.getDouble(latIndex);
            double longet = cursor.getDouble(longIndex);


            ((MainActivity) context).getClient().setProfilename(name);
            ((MainActivity) context).getClient().setLast(last);
            ((MainActivity) context).getClient().setFilename(filename);
            ((MainActivity) context).getClient().setDesc(desc);
            ((MainActivity) context).getClient().setImagename(icon);
            ((MainActivity) context).getClient().setLat(lat);
            ((MainActivity) context).getClient().setLonget(longet);
        }

        cursor.close();
        db.close();
    }

    public ArrayList<Client> getContactsData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {SQLHelper.CONTACTS_ID, SQLHelper.CONTACTS_NAME, SQLHelper.CONTACTS_LAST, SQLHelper.CONTACTS_DESCRIPTION, SQLHelper.CONTACTS_ICON, SQLHelper.CONTACTS_REC};
        Cursor cursor = db.query(SQLHelper.TABLE_NAME_CONTACTS, columns, null, null, null, null, null);
        ArrayList<Client> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_ID);
            int lastIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_LAST);
            int nameIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_NAME);
            int descIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_DESCRIPTION);
            int iconIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_ICON);
int reIndex=cursor.getColumnIndex(SQLHelper.CONTACTS_REC);
            int id = cursor.getInt(idIndex);
            String last = cursor.getString(lastIndex);
            String name = cursor.getString(nameIndex);
            String desc = cursor.getString(descIndex);
            String icon = cursor.getString(iconIndex);
            String rec=cursor.getString(reIndex);
            result.add(new Client(id, name, last, desc, icon,rec));
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<Client> getmapdata() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {SQLHelper.CONTACTS_ID, SQLHelper.CONTACTS_NAME, SQLHelper.CONTACTS_LAST, SQLHelper.CONTACTS_DESCRIPTION, SQLHelper.CONTACTS_LAT, SQLHelper.CONTACTS_LONGET};
        Cursor cursor = db.query(SQLHelper.TABLE_NAME_CONTACTS, columns, null, null, null, null, null);
        ArrayList<Client> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_ID);
            int lastIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_LAST);
            int nameIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_NAME);
            int latIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_LAT);
            int longetIndex = cursor.getColumnIndex(SQLHelper.CONTACTS_LONGET);
            int id = cursor.getInt(idIndex);
            String last = cursor.getString(lastIndex);
            String name = cursor.getString(nameIndex);
            double lat = cursor.getDouble(latIndex);
            double longet = cursor.getDouble(longetIndex);
            result.add(new Client(id, lat, longet, name, last));
        }
        cursor.close();
        db.close();
        return result;
    }

    public long insertDummyContact() {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id;
        Cursor c = db.rawQuery("SELECT " + SQLHelper.CONTACTS_ID + " FROM " + SQLHelper.TABLE_NAME_CONTACTS + " ORDER BY " + SQLHelper.CONTACTS_ID + " DESC LIMIT 1", null);
        if (c != null && c.moveToFirst()) {
            id = c.getLong(0) + 1;
        } else id = 0;
        c.close();
        db.close();
        return id;
    }

    public void addcontact(String name, String lastname, String description, double lat, double longet, String filename, String imagename) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.CONTACTS_NAME, name);
        contentValues.put(SQLHelper.CONTACTS_LAST, lastname);
        contentValues.put(SQLHelper.CONTACTS_DESCRIPTION, description);
        contentValues.put(SQLHelper.CONTACTS_LAT, lat);
        contentValues.put(SQLHelper.CONTACTS_LONGET, longet);
        contentValues.put(SQLHelper.CONTACTS_REC, filename);
        contentValues.put(SQLHelper.CONTACTS_ICON, imagename);
        db.insert(SQLHelper.TABLE_NAME_CONTACTS, null, contentValues);
        db.close();
    }

    public int deleteContact(int contactID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(contactID)};
        int count = db.delete(SQLHelper.TABLE_NAME_CONTACTS, SQLHelper.CONTACTS_ID + " =?", whereArgs);
        db.close();
        return count;


    }

    static class SQLHelper extends SQLiteOpenHelper {

        private Context context;
        private static final String DATABASE_NAME = "customer";
        private static final int DATABASE_VERSION = 8;

        // Table contacts
        private static final String TABLE_NAME_CONTACTS = "customer";
        private static final String CONTACTS_ID = "_id";
        private static final String CONTACTS_NAME = "name";
        private static final String CONTACTS_DESCRIPTION = "description";
        private static final String CONTACTS_ICON = "icon";
        private static final String CONTACTS_REC = "rec";
        private static final String CONTACTS_LAST = "last";
        private static final String CONTACTS_LAT = "lat";
        private static final String CONTACTS_LONGET = "longet";

        private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE IF not exists " + TABLE_NAME_CONTACTS + "("
                + CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CONTACTS_NAME + " VARCHAR(255), "
                + CONTACTS_LAST + " VARCHAR(255), "
                + CONTACTS_DESCRIPTION + " VARCHAR(255), "
                + CONTACTS_LAT + " VARCHAR(255), "
                + CONTACTS_LONGET + " VARCHAR(255), "
                + CONTACTS_ICON + " VARCHAR(255), " +
                CONTACTS_REC + " VARCHAR(255)" +
                ");";
        private static final String DROP_TABLE_CONTACTS = "DROP TABLE IF EXISTS " + TABLE_NAME_CONTACTS;

        public SQLHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            SQLiteDatabase db = this.getWritableDatabase();
            db.setForeignKeyConstraintsEnabled(true);
//            Toast.makeText(this.context, "Constructor called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_CONTACTS);
                Toast.makeText(context, "New version of database created", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE_CONTACTS);

                onCreate(db);
//                Toast.makeText(context, "The previous version of database dropped", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
