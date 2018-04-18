package com.example.ar2211.androidsampleras;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import android.widget.TextView;
import com.example.ar2211.androidsampleras.MainActivity;



import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class
MusicPads extends AppCompatActivity {
    private Button mBtGoBack;
    public int[] Triggers = new int[16];
    int currentplaying=-1;
    final MediaPlayer[] media = new MediaPlayer[16];
    Uri path;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_pads);

        Intent i = getIntent();
        String Trigger = "";
        //The second parameter below is the default string returned if the value is not there.
        Trigger = i.getStringExtra("key");
        path =  Uri.parse(i.getStringExtra("path"));
        Arrays.fill(Triggers,0);
        if(Trigger != null) {
            String delims = ",";
            String[] tokens = Trigger.split(delims);
            for(int x = 0; x < tokens.length; x++)
            {
                String number = tokens[x];
                Triggers[x] =  Integer.parseInt(number);
            }

            // Testing to make this code below not suck https://pastebin.com/KKMwGiYR

            for(int y = 0; y < 16; y++)
            {
                InitMp(y);
                media[y].seekTo(Triggers[y]);
            }

            Button button1 = (Button) findViewById(getResources().getIdentifier("button1", "id",
                    this.getPackageName()));
            Button button2 = (Button) findViewById(getResources().getIdentifier("button2", "id",
                    this.getPackageName()));
            Button button3 = (Button) findViewById(getResources().getIdentifier("button3", "id",
                    this.getPackageName()));
            Button button4 = (Button) findViewById(getResources().getIdentifier("button4", "id",
                    this.getPackageName()));
            Button button5 = (Button) findViewById(getResources().getIdentifier("button5", "id",
                    this.getPackageName()));
            Button button6 = (Button) findViewById(getResources().getIdentifier("button6", "id",
                    this.getPackageName()));
            Button button7 = (Button) findViewById(getResources().getIdentifier("button7", "id",
                    this.getPackageName()));
            Button button8 = (Button) findViewById(getResources().getIdentifier("button8", "id",
                    this.getPackageName()));
            Button button9 = (Button) findViewById(getResources().getIdentifier("button9", "id",
                    this.getPackageName()));
            Button button10 = (Button) findViewById(getResources().getIdentifier("button10", "id",
                    this.getPackageName()));
            Button button11 = (Button) findViewById(getResources().getIdentifier("button11", "id",
                    this.getPackageName()));
            Button button12 = (Button) findViewById(getResources().getIdentifier("button12", "id",
                    this.getPackageName()));
            Button button13 = (Button) findViewById(getResources().getIdentifier("button13", "id",
                    this.getPackageName()));
            Button button14 = (Button) findViewById(getResources().getIdentifier("button14", "id",
                    this.getPackageName()));
            Button button15 = (Button) findViewById(getResources().getIdentifier("button15", "id",
                    this.getPackageName()));
            Button button16 = (Button) findViewById(getResources().getIdentifier("button16", "id",
                    this.getPackageName()));

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(1);
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(2);
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(3);
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(4);
                }
            });
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(5);
                }
            });
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(6);
                }
            });
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(7);
                }
            });
            button8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(8);
                }
            });
            button9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(9);
                }
            });
            button10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(10);
                }
            });
            button11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(11);
                }
            });
            button12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(12);
                }
            });
            button13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(13);
                }
            });
            button14.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(14);
                }
            });
            button15.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(15);
                }
            });
            button16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playNewMedia(16);
                }
            });


            TextView txtInput2 = (TextView) findViewById(R.id.textView);
            txtInput2.setText(Trigger);
        }

        else {
            TextView txtInput2 = (TextView) findViewById(R.id.textView);
            txtInput2.setText("Trigger was null" );
        }
        //start converting the array of string to array of ints





        mBtGoBack = (Button) findViewById(R.id.bt_go_back);

        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }
    public void InitMp(int index) {
        try {
            media[index].setDataSource(this, path);
            media[index].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //player.start();
                }
            });
            media[index].prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void playNewMedia(int id){
        if(currentplaying!=-1) {
            if (media[currentplaying].isPlaying()) {
                media[currentplaying].pause();
                media[currentplaying].seekTo(Triggers[currentplaying]);
            }
        }
        media[id-1].start();
        currentplaying=id-1;
    }

}
