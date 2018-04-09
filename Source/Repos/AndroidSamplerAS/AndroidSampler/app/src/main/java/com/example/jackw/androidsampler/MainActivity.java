package com.example.jackw.androidsampler;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jackw.androidsampler.PlayerVisualizerView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static android.media.MediaPlayer.SEEK_NEXT_SYNC;

public class MainActivity extends AppCompatActivity {
    PlayerVisualizerView custView;
    Uri path;
    int time;
    final MediaPlayer mp = new MediaPlayer();
    boolean initvis=false;
    //TextView tv = (TextView)findViewById(R.id.textView);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final long period = 200;
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                if (initvis){
                    float progress=(float)mp.getCurrentPosition()/(float)mp.getDuration();
                    custView.updateLoc(progress);
                }
            }
        }, 0, period);

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

        Button showView=(Button) findViewById(R.id.buttonInitView);
        showView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                custView=findViewById(R.id.custView);
                File tempFile=new File(getPath(getApplicationContext(),path));
                byte[] bytes=fileToBytes(tempFile);
                custView.updateVisualizer(bytes);
                initvis=true;
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

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
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
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

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
            initvis=true;
        }
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


}