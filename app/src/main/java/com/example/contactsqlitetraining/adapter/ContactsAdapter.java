package com.example.contactsqlitetraining.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsqlitetraining.MainActivity;
import com.example.contactsqlitetraining.R;
import com.example.contactsqlitetraining.db.entity.Contact;
import com.example.contactsqlitetraining.view.ContactViewHolder;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private MainActivity mainActivity;
    private ArrayList<Contact> contacts;

    public ContactsAdapter(MainActivity mainActivity, ArrayList<Contact> contacts) {
        this.mainActivity = mainActivity;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        final Contact contact= contacts.get(position);
        holder._getName().setText(contact.getName());
        holder._getEmail().setText(contact.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.editDeleteContact(true ,contact, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
