package com.sked.razaform.view;


import android.content.Intent;
import android.media.MediaPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sked.razaform.R;

import java.io.IOException;


public class RingtoneActivity extends Activity {
    final String[] ringtone = new String[]{
            "ringtone 1", "ringtone 2", "ringtone 3",
            "ringtone 4","ringtone 5","ringtone 6",
            "ringtone 7","ringtone 8","ringtone 9",
            "ringtone 10","ringtone 11","ringtone 12",
            "ringtone 13","ringtone 14","ringtone 15"
        };
    private ListView ringtoneListView;

    private MediaPlayer mediaPlayer;
    private int[] ringtoneids = {
            R.raw.ringtone_1, R.raw.ringtone_2, R.raw.ringtone_3,
            R.raw.ringtone_4,R.raw.ringtone_5,R.raw.ringtone_6,
            R.raw.ringtone_7,R.raw.ringtone_8,R.raw.ringtone_9,
            R.raw.ringtone_10,R.raw.ringtone_11,R.raw.ringtone_12,
            R.raw.ringtone_13,R.raw.ringtone_14,R.raw.ringtone_15};
    private int selectedRingTone = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);
        ringtoneListView = (ListView) findViewById(R.id.ringtones);
        ringtoneListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        mediaPlayer = new MediaPlayer();


        ArrayAdapter<String> ringtoneAdapter;
        ringtoneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, ringtone);


        //Setting adapter to the ListView

        ringtoneListView.setAdapter(ringtoneAdapter);

        ringtoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRingTone = ringtoneids[position];
                playTone(selectedRingTone);

            }
        });

        //Getting data from Intent
        Intent intent = getIntent();
        int ringtoneId = intent.getIntExtra("audioId", selectedRingTone);
        setPreviousSelectedRingTone(ringtoneId);

    }

    /***
     * Sets the previously selected ring tone
     *
     * @param ringtoneId the ring tone id
     */
    private void setPreviousSelectedRingTone(int ringtoneId) {
        int position = getPositionById(ringtoneId);
        ringtoneListView.setItemChecked(position, true);
    }

    /**
     * Finding the position of ringtoneId in the array ringtoneids
     * @param ringtoneId which position is to be found
     * @return position of the ringtoneId in the array ringtoneids else returns -1 if not available
     */
    private int getPositionById(int ringtoneId) {
        for (int i = 0; i < ringtoneids.length; i++) {
            if (ringtoneId == ringtoneids[i])
                return i;
        }
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ringtone_service, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id=item.getItemId();
        if (item.getItemId() == R.id.action_choose) {
            mediaPlayer.stop();
            mediaPlayer.release();
            Intent intent = new Intent();
            intent.putExtra("audio_id", selectedRingTone);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    void playTone(int songsRawId) {
        try {
            mediaPlayer.reset();
            Uri dataSource = Uri.parse("android.resource://" + getPackageName()
                    + "/" + songsRawId);

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





