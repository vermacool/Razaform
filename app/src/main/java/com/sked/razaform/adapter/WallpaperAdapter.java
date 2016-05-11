package com.sked.razaform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sked.razaform.R;
import com.sked.razaform.model.Wallpaper;
import com.sked.razaform.view.CircularImageView;

import java.util.List;

import static android.widget.CompoundButton.*;

/**
 * Created by manish on 11/1/2015.
 */
public class WallpaperAdapter extends ArrayAdapter<Wallpaper> {
    private Context context;
    private int resource;
    private List<Wallpaper> wallpapers;


    public WallpaperAdapter(Context context, int resource, List<Wallpaper> wallpapers) {
        super(context, resource, wallpapers);
        this.context = context;
        this.resource = resource;
        this.wallpapers = wallpapers;

    }

    @Override
    public View getView(int position, View recycled, ViewGroup container) {
        //Getting the system's layout inflater from the System's Inflater Service
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflating the layout to get the view reference
        View itemView = inflater.inflate(resource, null);

        TextView name = (TextView) itemView.findViewById(R.id.name);
        CircularImageView image = (CircularImageView) itemView.findViewById(R.id.image);


        Wallpaper wallpaper = wallpapers.get(position);

        name.setText(wallpaper.getName());
        image.setImageResource(wallpaper.getImageId());

        return itemView;
    }
}
