package com.sked.razaform;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sked.razaform.adapter.MessageAdapter;
import com.sked.razaform.database.DataBase;
import com.sked.razaform.database.Table;
import com.sked.razaform.model.Message;
import com.sked.razaform.view.CircularImageView;
import com.sked.razaform.view.RingtoneActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends Activity {
    private static final int REQUEST_CODE_WALLPAPER = 1235;
    private static final int REQUEST_CODE_RINGTONE = 1236;
    private static final int SELECTED_IMAGE_ACTIVITY_REQUEST_CODE = 1237;
    ContentValues contentValues;
    EditText username;
    ImageView clickImage;
    private ListView messageList;
    private ImageView wallpaper;
    private ImageButton send, imagepicker;
    private EditText messageBox;
    private List<Message> messages;
    private ListView listView;
    private MessageAdapter messageAdapter;
    private DataBase db;
    private SQLiteDatabase sqLiteDatabase;
    public static int id;
    private MediaPlayer mediaPlayer;
    private Object SQLiteHelper;
    private int ringtoneId;
    private ImageSwitcher viewFlipper;
    private float lastX;
    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (messageBox.length() > 0) {
                //Creating an instance of Message Model from the messageBox's String
                Message message = new Message(messageBox.getText().toString());
                message.setType(Message.OUTGOING);
                message.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:yy", Locale.US).format(new Date()));
                //Adding the message instance to the List messages
                messages.add(message);
                //Notifying the adapter that List of data is changed
                messageAdapter.notifyDataSetChanged();
                messageBox.setText("");
                playTone(ringtoneId);
                insertData(message);
            }
        }


    };
    private int wallpaperId;
    private Toast toast;

    public void onGetImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select image"), SELECTED_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_chat);
        clickImage = (ImageView) findViewById(R.id.clickImage);
        Intent intent = getIntent();
        mediaPlayer = new MediaPlayer();
        id = intent.getIntExtra("id", -1);
        Toast.makeText(ChatActivity.this, ""+id, Toast.LENGTH_SHORT).show();
        ringtoneId = R.raw.ringtone_1;

        username = (EditText) findViewById(R.id.email);
        messageList = (ListView) findViewById(R.id.messages);
        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(ChatActivity.this, "list item is popping up" + position, Toast.LENGTH_SHORT);
                toast.show();
//                messages.remove(position);
                messageAdapter.notifyDataSetChanged();
            }
        });
        wallpaper = (ImageView) findViewById(R.id.wallpaper);
        send = (ImageButton) findViewById(R.id.send);
        messageBox = (EditText) findViewById(R.id.messageBox);
        messageBox.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(messageBox, inputMethodManager.SHOW_IMPLICIT);
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageBox.getWindowToken(), 0);
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.chat_item_left, messages,id);
        messageList.setAdapter(messageAdapter);
        send.setOnClickListener(onClickListener);
        init();

    }


    int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    private void init() {

        db = new DataBase(this);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Table.User.TABLE_NAME, null, Table.User.ID + " = ?", new String[]{String.valueOf(id)}, null, null, Table.User.ID + " DESC ");
        if (cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex(Table.User.ID);
            int idValue = cursor.getInt(indexId);

            int indexName = cursor.getColumnIndex(Table.User.USERNAME);
            String nameValue = cursor.getString(indexName);

            int indexImageName = cursor.getColumnIndex(Table.User.IMAGE);
            String imageName = cursor.getString(indexImageName);

            ActionBar actionBar = getActionBar();
            if (actionBar != null && imageName != null && !imageName.equalsIgnoreCase("")) {
                actionBar.setTitle(nameValue);
                //Getting bitmap from the imageName
                Bitmap bitmap = getImage(imageName);
                //Creating circular bitmap from bitmap
                Bitmap circularBitmap = CircularImageView.getCroppedBitmap(bitmap, 100);
                //Creating DrawableBitmap
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), circularBitmap);
                //Setting icon drawable to the ActionBar
                actionBar.setIcon(bitmapDrawable);
            }


            int indexMobileNumber = cursor.getColumnIndex(Table.User.MOBILENO);
            String mobileValue = cursor.getString(indexMobileNumber);

            int indexWallpaper = cursor.getColumnIndex(Table.User.WALLPAPER);
            int wallpaperId = cursor.getInt(indexWallpaper);

            if (wallpaperId > 0) {
                wallpaper.setImageResource(wallpaperId);

            }
            int indexRingtone = cursor.getColumnIndex(Table.User.RINGTONE);
            ringtoneId = cursor.getInt(indexRingtone);

            if (ringtoneId > 0) {


            }
        }
        cursor.close();
        loadMessages();
    }

    private void loadMessages() {

        db = new DataBase(this);
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Table.ChatData.TABLE_NAME, null
                , Table.ChatData.USER_ID + " = ?", new String[]{id + ""}, null, null, null);
        while (cursor.moveToNext()) {
            int indexId = cursor.getColumnIndex(Table.ChatData._ID);
            int idValue = cursor.getInt(indexId);

            int indexUserId = cursor.getColumnIndex(Table.ChatData.USER_ID);
            int UserIdValue = cursor.getInt(indexUserId);

            int indexMessage = cursor.getColumnIndex(Table.ChatData.MESSAGE);
            String messageValue = cursor.getString(indexMessage);

            int indexType = cursor.getColumnIndex(Table.ChatData.TYPE);
            int typeValue = cursor.getInt(indexType);

            int indexFileUri = cursor.getColumnIndex(Table.ChatData.FILE_URI);
            String fileUriValue = cursor.getString(indexFileUri);

            int indexTime = cursor.getColumnIndex(Table.ChatData.TIME);
            String timeValue = cursor.getString(indexTime);

            Message message = new Message(messageValue);
            message.setId(idValue);
            message.setTime(timeValue);
            message.setType(typeValue);
            if (fileUriValue != null) {
                message.setFileUri(Uri.parse(fileUriValue));
            }
            messages.add(message);
        }
        cursor.close();
        messageAdapter.notifyDataSetChanged();

    }

    //Getting bitmap form sdcard
    private Bitmap getImage(String imageName) {

        String path = Environment.getExternalStorageDirectory() + "/" + imageName;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }
/* public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }*/
    //    }
//        startActivity(Intent.createChooser(intent, "Select stream"));
//        intent.setType("*/*");
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//    public void onGetStream(View view) {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_change_wallpaper) {
            Intent intent = new Intent(this, WallpapersListActivity.class);
            //intent.putExtra("imageId", wallpaperId);
            startActivityForResult(intent, REQUEST_CODE_WALLPAPER);
            return true;
        }
        if (id == R.id.action_ringtone) {
            Intent intent = new Intent(this, RingtoneActivity.class);
            intent.putExtra("audioId", ringtoneId);
            //setResult(RESULT_OK,intent);
            startActivityForResult(intent, REQUEST_CODE_RINGTONE);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WALLPAPER && resultCode == RESULT_OK) {
            int wallpaperId = data.getIntExtra("wallpaperId", -1);
            wallpaper.setImageResource(wallpaperId);
            saveWallpaper(wallpaperId);
        } else if (requestCode == REQUEST_CODE_RINGTONE && resultCode == RESULT_OK) {

            int audioId = data.getIntExtra("audio_id", R.raw.ringtone_1);
            if (audioId > 0) {
                ringtoneId = audioId;
                updateRingtone(id, audioId);
            }

        } else if (requestCode == SELECTED_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //
            Uri uri = data.getData();
            Log.d("uri", uri.toString());

            Message message = new Message("Image");
            message.setFileUri(uri);
            message.setType(Message.OUTGOING);
            message.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:yy", Locale.US).format(new Date()));
            messages.add(message);
            messageAdapter.notifyDataSetChanged();
            insertData(message);

        }

    }

    private void insertData(Message message) {
        db = new DataBase(this);
        sqLiteDatabase = db.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(Table.ChatData.USER_ID, id);
        contentValues.put(Table.ChatData.TYPE, message.getType());
        contentValues.put(Table.ChatData.MESSAGE, message.getText());
        if (message.getFileUri() != null) {
            contentValues.put(Table.ChatData.FILE_URI, message.getFileUri().toString());
        }
        long insertedId = sqLiteDatabase.insert(Table.ChatData.TABLE_NAME, null, contentValues);
        Log.d("inserted", insertedId + "");

    }

    private void updateRingtone(int id, int audioId) {
        db = new DataBase(this);
        sqLiteDatabase = db.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.User.RINGTONE, audioId);
        sqLiteDatabase.update(Table.User.TABLE_NAME, contentValues, Table.User.ID + " = ?", new String[]{id + ""});
    }


    private void saveWallpaper(int wallpaperId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.User.WALLPAPER, wallpaperId);
        db = new DataBase(this);
        sqLiteDatabase = db.getWritableDatabase();
        long updated = sqLiteDatabase.update(Table.User.TABLE_NAME, contentValues, Table.User.ID + " = ?", new String[]{String.valueOf(id)});

        if (updated > 0) {
            Toast.makeText(getApplicationContext(), "Wallpaper Changed!", Toast.LENGTH_SHORT).show();
        }
    }


    void playTone(int songsRawId) {
        try {
            mediaPlayer.reset();
            Uri dataSource = Uri.parse("android.resource://" + getPackageName()
                    + "/" + songsRawId);
            //Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Songs.mp3");
            mediaPlayer.setDataSource(this, dataSource);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onBackPressed();
    }


}
