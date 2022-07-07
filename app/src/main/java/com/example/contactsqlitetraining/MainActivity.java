package com.example.contactsqlitetraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.contactsqlitetraining.adapter.ContactsAdapter;
import com.example.contactsqlitetraining.db.database.ContactsAppDatabase;
import com.example.contactsqlitetraining.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter adapter;
    private ArrayList<Contact> contacts= new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAppDatabase db ;
    private FloatingActionButton addContactFloatingButton;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        adapter= new ContactsAdapter(MainActivity.this, contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        addContactFloatingButton = findViewById(R.id.addContactActionButton);
        addContactFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
            }
        });
        db = Room.databaseBuilder(getApplicationContext(), ContactsAppDatabase.class, "ContactDB")
                .allowMainThreadQueries()// This is unacceptable must be removed to the background thread might throw an ANR in 5 seconds
                .build();
        fetchDataFromDatabaseAsync();




    }

    public void fetchDataFromDatabaseAsync(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                contacts.addAll(db.getContactDAO().getContacts());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void addContact() {
        View view = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.contact_add, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);
        TextView contactTitle = view.findViewById(R.id.textView);
        EditText name = view.findViewById(R.id.nameAdd);
        EditText email = view.findViewById(R.id.emailAdd);
        contactTitle.setText("Add New Contact");
        alertDialogBuilder.setCancelable(false).
                setPositiveButton("Save", saveButtonListener(name, email));
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    public void editDeleteContact( final Contact contact, final int position) {
        View view = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.contact_add, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);
        TextView contactTitle = view.findViewById(R.id.textView);
        EditText name = view.findViewById(R.id.nameAdd);
        EditText email = view.findViewById(R.id.emailAdd);
        contactTitle.setText("Edit/Delete Contact");
        name.setText(contact.getName());
        email.setText(contact.getEmail());
        alertDialogBuilder.setCancelable(false).
                setPositiveButton("Update", updateButtonListener(position, name, email)).
                setNegativeButton("Delete", deleteButtonListener(contact, position, name));
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @NonNull
    private DialogInterface.OnClickListener saveButtonListener(EditText name, EditText email) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                _createContact(name.getText().toString(), email.getText().toString());
                dialogInterface.cancel();
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener deleteButtonListener(Contact contact, int position, EditText name) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please Enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                _deleteContact(contact, position);
                dialogInterface.cancel();
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener updateButtonListener(int position, EditText name, EditText email) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please Enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                _updateContact(name.getText().toString(), email.getText().toString(), position);
                dialogInterface.cancel();
            }
        };
    }

    private void _createContact(String name, String email) {
        long id = db.getContactDAO().addContact(new Contact(name, email));
        Contact contact = db.getContactDAO().getContact(id);
        if(contact!=null){
            contacts.add(0,contact);
            adapter.notifyDataSetChanged();
        }
    }

    private void _updateContact(String name, String email, int position) {
        Contact contact =contacts.get(position);
        contact.setEmail(email);
        contact.setName(name);
        db.getContactDAO().updateContact(contact);
        contacts.set(position,contact);
        adapter.notifyDataSetChanged();
    }

    private void _deleteContact(Contact contact, int position) {
        contacts.remove(position);
        db.getContactDAO().deleteContact(contact);
        adapter.notifyDataSetChanged();

    }



}