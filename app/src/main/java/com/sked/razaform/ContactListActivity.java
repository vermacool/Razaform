package com.sked.razaform;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sked.razaform.adapter.ContactsAdapter;
import com.sked.razaform.database.DataBase;
import com.sked.razaform.database.Table;
import com.sked.razaform.model.Contact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ContactListActivity extends Activity {
    private static final int REQUEST_CODE_ADD_CONTACT = 1234;
    private DataBase db;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayAdapter<Contact> adapter;
    private List<Contact> contactList;
    private MediaPlayer mediaPlayer;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ContactListActivity.this, ChatActivity.class);
            Contact contact = contactList.get(position);
            intent.putExtra("id", contact.getId());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ListView listView = (ListView) findViewById(R.id.listView);
        contactList = new ArrayList<>();
        adapter = new ContactsAdapter(this, R.layout.contact_list_item, contactList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

        select();

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    private void select() {
        contactList.clear();
        db = new DataBase(this);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Table.User.TABLE_NAME, null, null, null, null, null, Table.User.ID + " DESC ");
        while (cursor.moveToNext()) {
            int indexId = cursor.getColumnIndex(Table.User.ID);
            int idValue = cursor.getInt(indexId);
            int indexName = cursor.getColumnIndex(Table.User.USERNAME);
            String nameValue = cursor.getString(indexName);
            int indexMobileNumber = cursor.getColumnIndex(Table.User.MOBILENO);
            String mobileValue = cursor.getString(indexMobileNumber);

            int indexImage = cursor.getColumnIndex(Table.User.IMAGE);
            String imageName = cursor.getString(indexImage);

            File file = new File(Environment.getExternalStorageDirectory() + "/" + imageName);

            Contact contact = new Contact(idValue, mobileValue, nameValue, Uri.fromFile(file));
            contactList.add(contact);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_contact) {
            Intent intent = new Intent(this, NewContactActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_CONTACT && resultCode == RESULT_OK) {
            select();
        }
    }

    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
