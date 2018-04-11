package com.example.ldg121.passingstufftest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText Stufftopass;
    Button Trigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stufftopass = (EditText) findViewById(R.id.editText);
        Trigger = (Button) findViewById(R.id.button);
        Trigger.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String temp = Stufftopass.getText().toString();
                Intent i = new Intent(getBaseContext(), RecievingStuff.class);
                i.putExtra("key", temp);

                startActivity(i);
            }

        });
    }

}



