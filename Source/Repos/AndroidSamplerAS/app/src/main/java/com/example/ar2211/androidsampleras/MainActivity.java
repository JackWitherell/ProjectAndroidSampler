package com.example.ar2211.androidsampleras;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class MainActivity extends AppCompatActivity {
    Uri path;
    int time;
    final MediaPlayer mp = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
            }
        });
    }


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            path = data.getData();
            setFileSource();
        }
    }

}