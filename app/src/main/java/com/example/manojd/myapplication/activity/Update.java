package com.example.manojd.myapplication.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.manojd.myapplication.R;
import com.example.manojd.myapplication.Utility;
import com.example.manojd.myapplication.db.DbHelper;
import com.example.manojd.myapplication.model.Contact;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Update extends AppCompatActivity {
    EditText firstName,lastName,phone,email;
    ImageButton image;
    private int REQUEST_CAMERA =0,SELECT_FILE=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        firstName = findViewById(R.id.editFirstName);
        lastName = findViewById(R.id.editLastName);
        phone = findViewById(R.id.editNumber);
        email = findViewById(R.id.editEmail);
        image=findViewById(R.id.imgbtn1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getData();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Update.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String userChoosenTask;
                boolean result= Utility.checkPermission(Update.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        image.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            destination.getAbsolutePath();
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(thumbnail);
    }

    public void getData(){
        Intent intent = getIntent();
        int id = intent.getIntExtra("position",0);
        DbHelper helper = new DbHelper(this, DbHelper.DB_NAME, null, DbHelper.DB_VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Contact> contacts = new ArrayList<>();

        String query = "SELECT * FROM " + DbHelper.TABLE_NAME + " WHERE ID=" + id;
        Cursor cursor = db.rawQuery(query, null);
        Contact contact = null;
        if (cursor.moveToFirst()) {
            do {
                contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setFirstName(cursor.getString(1));
                contact.setLastName(cursor.getString(2));
                contact.setMobile(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setImage(cursor.getString(5));


                contacts.add(contact);

            } while (cursor.moveToNext());
        }

        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        phone.setText(contact.getMobile());
        email.setText(contact.getEmail());
        if (contact.getImage() == null) {
            image.setImageResource(R.drawable.contacts_icon);
        } else {
            byte[] arr = Base64.decode(contact.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            image.setImageBitmap(bitmap);
        }

    }
    public void save(View view)
    {
        if (firstName.getText().toString().length() == 0 || lastName .getText().toString().length() == 0
                || phone .getText().toString().length() == 0  || email .getText().toString().length() == 0 )
        {

            Toast.makeText(Update.this, "please fill all the fields..", Toast.LENGTH_LONG).show();
        }
        else {
            //update database
            Intent intent = getIntent();
            int id = intent.getIntExtra("position",0);
            DbHelper helper = new DbHelper(this, DbHelper.DB_NAME, null, DbHelper.DB_VERSION);
            SQLiteDatabase db = helper.getReadableDatabase();
            ContentValues values = new ContentValues();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bm=((BitmapDrawable)image.getDrawable()).getBitmap();
            bm.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] arr = stream.toByteArray();
            String result = Base64.encodeToString(arr,Base64.DEFAULT);


            values.put(DbHelper.COLUMN_2, firstName.getText().toString());
            values.put(DbHelper.COLUMN_3, lastName.getText().toString());
            values.put(DbHelper.COLUMN_4, phone.getText().toString());
            values.put(DbHelper.COLUMN_5, email.getText().toString());
            values.put(DbHelper.COLUMN_6,result);
            db.update(DbHelper.TABLE_NAME, values, "ID=" +id,null);
            Toast.makeText(Update.this, "Update Successfully..", Toast.LENGTH_LONG).show();
            finish();
        }

    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void cancel(View view){

        firstName.setText(null);
        lastName.setText(null);
        phone.setText(null);
        email.setText(null);

    }

}
