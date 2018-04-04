package com.example.ar2211.androidsampleras;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import java.util.*;
import android.widget.TextView;
import java.util.ArrayList;

import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class MusicPads extends AppCompatActivity {
    private Button mBtGoBack;
    ArrayList<String> TriggerHappy = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_pads);

        Bundle bundleTriggers = getIntent().getExtras();
        TriggerHappy = (ArrayList<String>) bundleTriggers.getSerializable("Triggers");
        TextView textView1 = (TextView) findViewById(R.id.display_trigger);

        textView1.setText( TriggerHappy.get(0));

        mBtGoBack = (Button) findViewById(R.id.bt_go_back);

        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        Button Music_pad_1 = (Button) findViewById(R.id.pad_1);

        Music_pad_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


            }
        });



    }

}
