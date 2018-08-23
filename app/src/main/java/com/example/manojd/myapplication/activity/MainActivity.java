package com.example.manojd.myapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.manojd.myapplication.R;
import com.example.manojd.myapplication.adapter.AdapterRecycler;
import com.example.manojd.myapplication.db.DbHelper;
import com.example.manojd.myapplication.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //http://www.androhub.com/android-read-contacts-using-content-provider/

    //ListView listView;
    RecyclerView recyclerView;
    ArrayList<Contact> contacts = new ArrayList<>();
    //ListAdapter adapter;
    AdapterRecycler adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveFlaotingButton);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
              {
                  Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
           }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(this,AddActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Contact> getData(){
        contacts.clear();
        DbHelper helper = new DbHelper(this,DbHelper.DB_NAME,null,DbHelper.DB_VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Contact> contacts = new ArrayList<>();

        String query = "SELECT * FROM "+DbHelper.TABLE_NAME + " ORDER BY " +DbHelper.COLUMN_2 + " ASC ";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setFirstName(cursor.getString(1));
                contact.setLastName(cursor.getString(2));
                contact.setMobile(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setImage(cursor.getString(5));
                contacts.add(contact);

            }while (cursor.moveToNext());
        }
        return contacts;
    }
    public void onResume()
    {  // After a pause OR at startup

        super.onResume();

        contacts = getData();

/////////////////////////////////




        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AdapterRecycler(this,contacts);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

      /*  listvoiew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contacts.get(position);
                Intent intent = new Intent(MainActivity.this,ContactDetails.class);
                intent.putExtra("position",contact);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"item clicked"+position,Toast.LENGTH_SHORT).show();

            }
        });*/
}


    @Override
    protected void onPause() {
        super.onPause();

    }
}
