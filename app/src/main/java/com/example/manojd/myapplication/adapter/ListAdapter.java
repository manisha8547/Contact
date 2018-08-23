package com.example.manojd.myapplication.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.manojd.myapplication.R;
import com.example.manojd.myapplication.model.Contact;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Contact> {
    ArrayList<Contact> contacts;
    public ListAdapter(@NonNull Context context, int resource, ArrayList<Contact> contacts) {
        super(context, resource);
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.contact_list_items,null);
        }

        Contact contact = contacts.get(position);

        if(contact != null){
            TextView textName = view.findViewById(R.id.textName);
            TextView textNumber = view.findViewById(R.id.textNumber);

            if(textName != null)
                textName.setText(contact.getFirstName());
            if(textNumber != null)
                textNumber.setText(contact.getMobile());
        }

        return view;
    }

}
