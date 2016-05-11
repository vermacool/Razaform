package com.sked.razaform.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sked.razaform.ChatActivity;
import com.sked.razaform.ImageActivity;
import com.sked.razaform.R;
import com.sked.razaform.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manish on 11/1/2015.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    public static List<Message> messages;
    int id;
    private ImageView image;
    private Context context;
    private int resource;

    public MessageAdapter(ChatActivity context, int resource, List<Message> messages, int id) {
        super(context, resource, messages);
        this.context = context;
        this.resource = resource;
        this.messages = messages;
        this.id = id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(resource, null);

        TextView text = (TextView) itemView.findViewById(R.id.text1);
        ImageView image = (ImageView) itemView.findViewById(R.id.image);


        Message message = messages.get(position);
        text.setText(message.getText());
        final Uri uri = message.getFileUri();

        if (uri != null) {
            image.setVisibility(View.VISIBLE);
            //image.setImageURI(uri);
            Picasso.with(context)
                    .load(uri).resize(dpToPx(250), dpToPx(200)).centerCrop()
                    .into(image);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
        return itemView;
    }

    int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
