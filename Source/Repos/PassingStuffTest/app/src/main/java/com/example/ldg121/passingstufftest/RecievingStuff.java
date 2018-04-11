package com.example.ldg121.passingstufftest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class RecievingStuff extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recieving_stuff);


        Intent i = getIntent();
        //The second parameter below is the default string returned if the value is not there.
        String txtData = i.getExtras().getString("key","");
        TextView txtInput2 = (TextView)findViewById(R.id.textView);

        txtInput2.setText(txtData);

    }

}
