package com.sked.razaform;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.sked.razaform.common.Utils;
import com.sked.razaform.database.DataBase;
import com.sked.razaform.database.Table;
import com.sked.razaform.net.MultipartRequest;
import com.sked.razaform.view.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class NewContactActivity extends Activity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1234;
    EditText username;
    EditText mobileno;
    CircularImageView image;
    String imageName;
    Button submit;
    DataBase db;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    private ArrayAdapter<String> adapter;
   // List<String> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raza_form);
        image=(CircularImageView)findViewById(R.id.image);
        username = (EditText) findViewById(R.id.email);
        mobileno = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!username.getText().toString())) {
                    username.setError("username is not valid !");
                    return;
                }*/
                if (mobileno.length() < 10) {
                    mobileno.setError("Mobile Number Should must be of 10 characters!");
                    return;
                }

                insertData();
                username.setError(null);
                username.setText("");
                mobileno.setError(null);
                mobileno.setText("");
                Toast.makeText(getApplicationContext(), "Contact Added !", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);

                finish();

            }
        });



    }

    private void insertData() {
        db = new DataBase(this);
        sqLiteDatabase = db.getReadableDatabase();
        contentValues = new ContentValues();
        contentValues.put(Table.User.USERNAME, username.getText().toString());
        contentValues.put(Table.User.MOBILENO, mobileno.getText().toString());
        contentValues.put(Table.User.IMAGE, imageName);
        contentValues.put(Table.User.RINGTONE, R.raw.ringtone_1);
        sqLiteDatabase.insert(Table.User.TABLE_NAME, null, contentValues);
        uploadImage();

    }


    private void startCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(); // create a file to save the image

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private Uri getOutputMediaFileUri() {
        //Creating a random name for the image file
        imageName = UUID.randomUUID() + ".jpeg";
        File imageFile = new File(Environment.getExternalStorageDirectory() + "/" + imageName);//Creating the file insatance
        Uri fileUri = Uri.fromFile(imageFile);//Creating Uri instance from file
        return fileUri;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_raza_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            setImage(imageName);
        }
    }

    private void setImage(String imageName) {
        String path = Environment.getExternalStorageDirectory() + "/" + imageName;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        }
    }

    private void uploadImage() {
        Map<String, String> params = new HashMap<>();
        params.put("imageName", "");
        Map<String, File> files = new HashMap<>();
        files.put("image", new File(Environment.getExternalStorageDirectory()+"/" + imageName));
        MultipartRequest multipartRequest = new MultipartRequest("http://54.149.13.57/profile/profileUpload.php" , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("Error: ", error.toString());
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                JSONObject response = null;
                try {
                    Log.d("jsonResponse: ", jsonResponse);
                    response = new JSONObject(jsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (response != null && response.has("statusmessage") && response.getBoolean("statusmessage")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, files, params);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(multipartRequest);
    }

}

    /*private void setImage(CircularImageView image) {
    }
}*/
