package com.example.manojd.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manojd.myapplication.R;
import com.example.manojd.myapplication.activity.ContactDetails;
import com.example.manojd.myapplication.model.Contact;

import java.util.ArrayList;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder> {
    private final Context context;
    private final ArrayList<Contact> contacts;

    public AdapterRecycler(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater inflater = LayoutInflater.from(context);
        //View layout =  inflater.inflate(R.layout.contact_list_items,null);
       // return new  ViewHolder(layout);
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_items,parent,false);
        ViewHolder holder = new ViewHolder(view,context,contacts);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Contact contact =contacts.get(position);
        holder.fName.setText(contact.getFirstName());
        holder.mobile.setText(contact.getMobile());
     if(contact.getImage() == null)
    {
           holder.imageView.setImageResource(R.drawable.contacts_icon);
     } else{
          byte [] arr = Base64.decode(contact.getImage(),Base64.DEFAULT);
         Bitmap bitmap = BitmapFactory.decodeByteArray(arr,0,arr.length);
          holder.imageView.setImageBitmap(bitmap); }
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView fName,mobile;
        ArrayList<Contact>contacts = new ArrayList<Contact>();
        Context context;
        public ViewHolder(View itemView,Context context,ArrayList<Contact>contacts) {
            super(itemView);
            this.contacts=contacts;
            this.context=context;
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.imageView);
            fName = itemView.findViewById(R.id.textName);
            mobile = itemView.findViewById(R.id.textNumber);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Contact contact = this.contacts.get(position);

            Intent intent= new Intent(this.context, ContactDetails.class);
            intent.putExtra("position",contact.getId());
            this.context.startActivity(intent);
        }
    }
}
