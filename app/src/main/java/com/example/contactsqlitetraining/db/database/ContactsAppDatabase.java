package com.example.contactsqlitetraining.db.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contactsqlitetraining.db.dao.ContactDAO;
import com.example.contactsqlitetraining.db.entity.Contact;


@Database(entities = {Contact.class}, version=1)
public abstract class ContactsAppDatabase extends RoomDatabase {

    public abstract ContactDAO getContactDAO();


}
