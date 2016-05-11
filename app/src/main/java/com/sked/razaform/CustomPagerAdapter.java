package com.sked.razaform;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sked.razaform.adapter.MessageAdapter;
import com.sked.razaform.database.DataBase;
import com.sked.razaform.database.Table;
import com.sked.razaform.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manish on 12/18/2015.
 */
class CustomPagerAdapter extends PagerAdapter {
        MessageAdapter messageAdapter;
        Context mContext;
        LayoutInflater mLayoutInflater;
private List<Message> messages = new ArrayList<>();

public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        messageAdapter = new MessageAdapter(context, R.layout.clickedimageitem);
        messages.clear();
        Intent intent= new Intent();
        int id = intent.getIntExtra("id",1);
        loadMessages(id);
        }

@Override
public int getCount() {
        return messages.size();
        }

@Override
public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
        }

@Override
public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.clickedimageitem, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.clickedImage);


        Message message = messages.get(position);
final Uri uri = message.getFileUri();
        Picasso.with(mContext)
        .load(uri).resize(1200, 1200)
        .into(imageView);
        container.addView(itemView);

        return itemView;
        }

@Override
public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
        }

        int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
        }

private void loadMessages(int id) {

        DataBase db = new DataBase(mContext);
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Table.ChatData.TABLE_NAME, null
        , Table.ChatData.USER_ID + " = ?", new String[]{id + ""}, null, null, null);
        while (cursor.moveToNext()) {


//            int indexMessage = cursor.getColumnIndex(Table.ChatData.MESSAGE);
//            String messageValue = cursor.getString(indexMessage);
//
//            int indexType = cursor.getColumnIndex(Table.ChatData.TYPE);
//            int typeValue = cursor.getInt(indexType);

        int indexFileUri = cursor.getColumnIndex(Table.ChatData.FILE_URI);
        String fileUriValue = cursor.getString(indexFileUri);


        Message message = new Message();

        if (fileUriValue != null) {
        message.setFileUri(Uri.parse(fileUriValue));
        }
        messages.add(message);
        }
        Toast.makeText(mContext, "" + messages.get(0).getFileUri(), Toast.LENGTH_SHORT).show();
        cursor.close();

        }
        }
