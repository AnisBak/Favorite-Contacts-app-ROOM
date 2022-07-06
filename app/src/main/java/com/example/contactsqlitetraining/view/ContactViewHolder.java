package com.example.contactsqlitetraining.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsqlitetraining.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView email;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.name);
        this.email = itemView.findViewById(R.id.email);
    }

    public TextView _getName() {
        return name;
    }

    public TextView _getEmail() {
        return email;
    }
}
