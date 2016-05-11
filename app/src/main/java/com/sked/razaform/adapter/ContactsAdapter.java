package com.sked.razaform.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sked.razaform.R;
import com.sked.razaform.model.Contact;
import com.sked.razaform.model.Message;
import com.sked.razaform.view.CircularImageView;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.List;

/**
 * Created by manish on 11/1/2015.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private List<Contact> contacts;

    public ContactsAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        this.context = context;
        this.resource = resource;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(resource, null);
        Contact contact = contacts.get(position);

        CircularImageView iconImage = (CircularImageView) itemView.findViewById(R.id.contacts_image);
        //  icon_image.setImageURI(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/" + imageName)));
        Uri uri = contact.getFileUri();
        if (uri != null) {
            //icon_image.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/" + icon_image));
            /*Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
            iconImage.setImageBitmap(bitmap);*/
            iconImage.setImageURI(uri);
        }
        TextView name = (TextView) itemView.findViewById(R.id.name);
        name.setText(contact.getName());

        TextView mobileNo = (TextView) itemView.findViewById(R.id.mobileNo);

        mobileNo.setText(contact.getMobileNumber());

        return itemView;
    }
}
