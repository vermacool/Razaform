package com.sked.razaform;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sked.razaform.adapter.WallpaperAdapter;
import com.sked.razaform.model.Wallpaper;

import java.util.ArrayList;
import java.util.List;

public class WallpapersListActivity extends Activity {

    private ListView wallpaperListView;
    private List<Wallpaper> wallpapersList;
    private WallpaperAdapter wallpaperAdapter;
    private int[] imageIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpapers_list);
        wallpaperListView = (ListView) findViewById(R.id.wallpapers);

        //Creating the name array
        String[] wallpapersName = new String[]{"wallpaper 1",
                "wallpaper 2", "wallpaper 3", "wallpaper 4",
                "wallpaper 5", "wallpaper 6", "wallpaper 7", "wallpaper 8", "wallpaper 9",
                "wallpaper 10", "wallpaper 11"};
        //Creating array of image ids stored in drawables

        imageIds = new int[]{R.drawable.im_wallpaper_1,
                R.drawable.im_wallpaper_2, R.drawable.im_wallpaper_3,
                R.drawable.im_wallpaper_4, R.drawable.im_wallpaper_5, R.drawable.im_wallpaper_6,
                R.drawable.im_wallpaper_7,
                R.drawable.im_wallpaper_8, R.drawable.im_wallpaper_9, R.drawable.im_wallpaper_10, R.drawable.im_wallpaper_11};
        //Creating the List of Wallpapers
        wallpapersList = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            Wallpaper wallpaper = new Wallpaper(imageIds[i], wallpapersName[i]);
            wallpapersList.add(wallpaper);
        }

        wallpaperAdapter = new WallpaperAdapter(this, R.layout.wallpaper_list_item, wallpapersList);

        wallpaperListView.setAdapter(wallpaperAdapter);
        wallpaperListView.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.putExtra("wallpaperId", imageIds[position]);
            setResult(RESULT_OK, intent);
            finish();
        }
    };


 /*       Intent intent=getIntent();
        int imageId = intent.getIntExtra("imageId",selectedImage);
        setPreviousSelectedImage(imageId);
    }

    private void setPreviousSelectedImage(int imageid) {
        int position = getPositionById(imageid);
        wallpaperListView.setItemChecked(position,true);

    }

    private int getPositionById(int imageid) {
        for (int i=0; i<imageIds.length; i++){
            if(imageid==imageIds[i])
                return i;
        }
        return -1;
    }
*/

}
