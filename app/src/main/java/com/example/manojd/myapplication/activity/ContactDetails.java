package com.example.manojd.myapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manojd.myapplication.R;
import com.example.manojd.myapplication.db.DbHelper;
import com.example.manojd.myapplication.model.Contact;

public class ContactDetails extends AppCompatActivity {
    TextView firstname,lastname,phoneNumber,emailId;
    ImageView image,call,message,sendEmail;
    Contact contact=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
       // Contact contact = intent.getParcelableExtra("position");


        int id=intent.getIntExtra("position",0);
        firstname = findViewById(R.id.tname);
        lastname = findViewById(R.id.tlname);
        phoneNumber=findViewById(R.id.tphone);
        emailId =findViewById(R.id.temail);
        image= findViewById(R.id.img);
        call=findViewById(R.id.call);
        message=findViewById(R.id.mesg);
        sendEmail=findViewById(R.id.sendEmail);

//        firstname.setText(contact.getFirstName());
//        lastname.setText(contact.getLastName());
//        phoneNumber.setText(contact.getMobile());
//        emailId.setText(contact.getEmail());

        firstname.setText(id +"");
       // contacts.clear();
        DbHelper helper = new DbHelper(this,DbHelper.DB_NAME,null,DbHelper.DB_VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT * FROM "+DbHelper.TABLE_NAME+ " WHERE id = "+id;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setFirstName(cursor.getString(1));
                contact.setLastName(cursor.getString(2));
                contact.setMobile(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setImage(cursor.getString(5));


            }while (cursor.moveToNext());
        }
        firstname.setText(contact.getFirstName());
        lastname.setText(contact.getLastName());
        phoneNumber.setText(contact.getMobile());
        emailId.setText(contact.getEmail());
        if(contact.getImage() == null)
        {
            image.setImageResource(R.drawable.contacts_icon);
        } else{
            byte [] arr = Base64.decode(contact.getImage(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(arr,0,arr.length);
            image.setImageBitmap(bitmap);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
      //  actionBar.setTitle(contact.getFirstName());

        //call activity
   call.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//               Intent callIntent = new Intent(Intent.ACTION_CALL);
//               callIntent.setData(Uri.parse("tel:123456789"));
//               startActivity(callIntent);
          Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + contact.getMobile()));
          startActivity(i);
      }
    });

    //message activity
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + contact.getMobile())));


            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaill=contact.getEmail();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",emaill, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
     return super.onOptionsItemSelected(item);
    }

    public void delete(View view){

        Intent intent = getIntent();
        int id=intent.getIntExtra("position",0);
        DbHelper helper = new DbHelper(this,DbHelper.DB_NAME,null,DbHelper.DB_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        int result = db.delete(DbHelper.TABLE_NAME,"ID="+id,null);
         //db.delete(DbHelper.TABLE_NAME,contact.getId(),null);
        Toast.makeText(this,"Item Deleted ",Toast.LENGTH_LONG).show();
         db.close();

        finish();
    }
    public void update(View view){
        Intent intent = getIntent();
        int id = intent.getIntExtra("position",0);
        Intent intent2 = new Intent(ContactDetails.this,Update.class);
        intent2.putExtra("position",id);
        startActivity(intent2);

        Toast.makeText(this,"Update clicked ",Toast.LENGTH_LONG).show();
        finish();
    }
}
