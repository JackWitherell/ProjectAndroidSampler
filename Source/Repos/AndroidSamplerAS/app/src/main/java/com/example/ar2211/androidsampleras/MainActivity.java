package com.example.ar2211.androidsampleras;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.*;
import java.io.Serializable;

import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class MainActivity extends AppCompatActivity implements Serializable{
    Uri path;
    int time;
    final MediaPlayer mp = new MediaPlayer();
    private Button mBtLaunchActivity;
    String Triggers = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtLaunchActivity = (Button) findViewById(R.id.bt_launch_activity);
        mBtLaunchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = Triggers;
                Intent i = new Intent(getBaseContext(), MusicPads.class);
                i.putExtra("key", temp);
                i.putExtra("path", path.toString());
                startActivity(i);

                //launchActivity();
            }
        });



        Button buttonChooseFile = (Button) findViewById(R.id.button);
        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                getFilePath();
            }

        });

        Button buttonStartPlayback= (Button) findViewById(R.id.buttonplay);
        buttonStartPlayback.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                if(mp.isPlaying()){
                    mp.pause();
                }
                else {
                    mp.start();
                }
            }
        });

        Button buttonRewind=(Button) findViewById(R.id.buttonRewind);
        buttonRewind.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mp.seekTo(0);
            }
        });

        Button buttonff=(Button) findViewById(R.id.button5sff);
        buttonff.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mp.seekTo(time, SEEK_NEXT_SYNC);
            }
        });

        Button buttonset=(Button) findViewById(R.id.buttonsetpos);
        buttonset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                time=mp.getCurrentPosition();
                buildString(Integer.toString(time));
            }
        });
    }

    //https://www.youtube.com/watch?v=JXjOxy2W_w0 used this to pass array list
    public void buildString(String text){
        if (Triggers == "")
        {
            Triggers = Triggers + text;
        }
        else
        {
            Triggers = Triggers + "," + text;
        }
    }
    /*private void launchActivity() {

        Intent intent = new Intent(this, MusicPads.class);
        startActivity(intent);
    }*/

    void setFileSource() {
        try {
            mp.setDataSource(this,path);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                public void onPrepared(MediaPlayer player){
                    //player.start();
                }
            });
            mp.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void getFilePath(){
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "DEMO"),1001);
    }

    public MediaPlayer getMediaPlayer()
    {
        return mp;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            path = data.getData();
            setFileSource();
        }
    }



}