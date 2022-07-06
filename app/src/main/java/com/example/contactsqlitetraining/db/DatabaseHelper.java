package com.example.contactsqlitetraining.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.contactsqlitetraining.db.entity.Contact;

import java.util.ArrayList;

public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact_db";
    private SQLiteDatabase db;
    private Contact contact;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contact.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long _addOne(String name, String email){
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, name);
        values.put(Contact.COLUMN_EMAIL, email);
        long id = db.insert(Contact.TABLE_NAME,null, values);
        db.close();
        return id;

    }

    public Contact _getOne(long id){

        db = getReadableDatabase();
        Cursor cursor = db.query(Contact.TABLE_NAME,
                new String[]{
                        Contact.COLUMN_ID,
                        Contact.COLUMN_NAME,
                        Contact.COLUMN_EMAIL
        },
                Contact.COLUMN_ID+"=?",
                new String[]{
                        String.valueOf(id)
                },null,null, null,null);
        if (cursor!=null) {
            cursor.moveToFirst();
            contact = new Contact(
                    cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));

        }
        cursor.close();
        return contact;
    }

    public ArrayList<Contact> _getAll(){
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+
                Contact.TABLE_NAME+" ORDER BY "+ Contact.COLUMN_ID+" DESC";

        db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do{
                contact = new Contact();
                contact.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)));
                contacts.add(contact);
                }while(cursor.moveToNext());
        }
        db.close();
        return contacts;
    }

    public int _updateOne(Contact contact){
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_ID, contact.getId());
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());
        return db.update(Contact.TABLE_NAME, values,Contact.COLUMN_ID+" =? ",
                new String[]{String.valueOf(contact.getId())});

    }

    public int _deleteOne(Contact contact){
        db = getWritableDatabase();
        return db.delete(Contact.TABLE_NAME, Contact.COLUMN_ID+" =? ",
                new String[]{String.valueOf(contact.getId())});
    }
}
