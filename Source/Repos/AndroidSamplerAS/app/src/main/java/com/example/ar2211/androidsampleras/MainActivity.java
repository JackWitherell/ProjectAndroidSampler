package com.example.ar2211.androidsampleras;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class MainActivity extends AppCompatActivity implements Serializable{
    PlayerVisualizerView custView;
    Uri path;
    int time;
    public float intensity = 0;
    final MediaPlayer mp = new MediaPlayer();
    Visualizer vis;
    boolean initvis=false;
    private Button mBtLaunchActivity;
    String Triggers = "";

    private int RECORD_AUDIO_PERMISSION_CODE=23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, permissions,23);
        }

        final long period = 200;
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                if (initvis){

                }
            }
        }, 0, period);

        mBtLaunchActivity = (Button) findViewById(R.id.bt_launch_activity);
        mBtLaunchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = Triggers;
                Intent i = new Intent(getBaseContext(), MusicPads.class);
                i.putExtra("key", temp);
                i.putExtra("path", path);
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

    public void RecordAudioPersmission(View view){
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        {
            getFilePath();
        }
        else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(this, "Need in order to record your own audio", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO_PERMISSION_CODE);
        }
    }

    public void ReadExternalStoragePersmission(View view){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            getFilePath();
        }
        else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Need in order to upload your own sounds", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RECORD_AUDIO_PERMISSION_CODE);
        }
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
            Log.d("emm:", path.toString());
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

    private void initVisualizer() {
        vis=new Visualizer(mp.getAudioSessionId());
        vis.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                intensity = ((float) waveform[0] + 128f) / 256;
                custView.updateLoc((float)mp.getCurrentPosition()/(float)mp.getDuration(), intensity);
                Log.d("vis", String.valueOf(intensity));
            }
            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate(), true, false);
    }

    public static byte[] fileToBytes(File file) {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            path = data.getData();
            setFileSource();

            custView=findViewById(R.id.custView);
            File tempFile=new File(getPath(getApplicationContext(),path));
            byte[] bytes=fileToBytes(tempFile);
            custView.updateVisualizer(bytes);
            initVisualizer();
            vis.setEnabled(true);
            initvis=true;
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getPath(Context context, Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        return "-1";
    }





}