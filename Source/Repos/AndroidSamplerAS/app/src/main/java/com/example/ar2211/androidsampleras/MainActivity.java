package com.example.ar2211.androidsampleras;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecordAudioPersmission();
        ReadExternalStoragePersmission();
        final long period = 200;
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                if (initvis){

                }
            }
        }, 0, period);

        mBtLaunchActivity = findViewById(R.id.bt_launch_activity);
        mBtLaunchActivity.setEnabled(false);
        mBtLaunchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = Triggers;
                Intent i = new Intent(getBaseContext(), MusicPads.class);
                i.putExtra("key", temp);
                i.putExtra("path", path);
                startActivity(i);
            }
        });

        final Button buttonStartPlayback= findViewById(R.id.buttonplay);
        buttonStartPlayback.setEnabled(false);
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

        final Button buttonRewind= findViewById(R.id.buttonRewind);
        buttonRewind.setEnabled(false);
        buttonRewind.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mp.seekTo(0);
            }
        });

        final Button buttonff= findViewById(R.id.buttonseek);
        buttonff.setEnabled(false);
        buttonff.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mp.seekTo(time, SEEK_NEXT_SYNC);
            }
        });

        final Button buttonset= findViewById(R.id.buttonsetpos);
        buttonset.setEnabled(false);
        buttonset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                time=mp.getCurrentPosition();
                buildString(Integer.toString(time));
            }
        });
        Button buttonChooseFile = findViewById(R.id.buttonchoosefile);
        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                getFilePath();
                buttonset.setEnabled(true);
                buttonff.setEnabled(true);
                buttonStartPlayback.setEnabled(true);
                buttonRewind.setEnabled(true);
                mBtLaunchActivity.setEnabled(true);
            }
        });
    }

    public void RecordAudioPersmission(){
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(this, "Need in order to record your own audio", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},22);
        }
    }

    public void ReadExternalStoragePersmission(){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Need in order to upload your own sounds", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 23);
        }
    }

    public void buildString(String text){
        if (Triggers == "") {
            Triggers = Triggers + text;
        }
        else {
            Triggers = Triggers + "," + text;
        }
    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            path = data.getData();
            setFileSource();

            custView=findViewById(R.id.custView);
            custView.updateVisualizer();
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
        if (DocumentsContract.isDocumentUri(context, uri)) {
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