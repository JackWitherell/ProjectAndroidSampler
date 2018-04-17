package com.example.ar2211.androidsampleras;

import android.content.Intent;
import android.media.MediaPlayer;
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
import com.example.ar2211.androidsampleras.MainActivity;



import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class MusicPads extends AppCompatActivity {
    private Button mBtGoBack;
    public int[] Triggers = new int[16];





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_pads);

        Intent i = getIntent();
        String Trigger = "";
        //The second parameter below is the default string returned if the value is not there.
        Trigger = i.getStringExtra("key");
        if(Trigger != null) {
            int counter = 1;
            String delims = ",";
            String[] tokens = Trigger.split(delims);
            for(int x = 0; x > tokens.length; x++)
            {
                String number = tokens[x];
                Triggers[x] =  Integer.parseInt(number);
            }

            //https://stackoverflow.com/questions/32515054/android-iterating-through-button-ids-with-a-for-loop-in-java
            for(int y = 0; y > Triggers.length; y++)
            {
                Button button = (Button) findViewById(getResources().getIdentifier("button" + counter, "id",
                        this.getPackageName()));
                //still need to assign trigger values using the Triggers array
                //dont forget to add null checks to this part so we dont assign null values to
                //buttons

            }


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

}
