package net.jp.garlands.simpleandroidplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TrackActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static Activity me = null;
    private TrackAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MediaPlayer mp;
    private String AlbumId;
    private String AlbumArtURL;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 2;

    List<TrackListData> myListData = new ArrayList<>();

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        me = this;

        Intent intent = getIntent();
        AlbumId = intent.getStringExtra("com.example.simpleandroidplayer.albumsID");

        recyclerView = (RecyclerView) findViewById(R.id.track_recycler_view);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TrackListData trc = myListData.get(position);
                String path = trc.getData();
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    mp.release();
                    mp= null;
                }
                try {
                    mp = new MediaPlayer();
                    mp.setDataSource(path);
                    mp.prepareAsync();
//                    mp.setLooping(true);
                    mp.seekTo(0);
                    mp.setVolume(0.5f, 0.5f);
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                }catch (Exception e){
                    Log.d("TRACK","cannt MediaPlayer");
                }
            }
        };

        if( checkPermission() == true ) {
            getTrackList();
        }
        mAdapter = new TrackAdapter(this.getApplicationContext(),
                    R.layout.layout_track_listitem,
                    myListData,
                    AlbumArtURL,
                    itemClickListener);
        recyclerView.setAdapter(mAdapter);
        recyclerView.requestFocus();
    }

    private static boolean checkPermission() {
        Log.i("CAMERA", "Enter checkPermission()");
        int permission1 = ContextCompat.checkSelfPermission(me, Manifest.permission.MEDIA_CONTENT_CONTROL);
        if( permission1 != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(
                    me,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Log.i("ALBUM", " getExternalStorageState " + status);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getTrackList();
                }
                return;
            }
        }
    }

    private void getTrackList(){
        Log.i("TRACK", "Enter getAlbumList()");
        ContentResolver resolver = me.getContentResolver();

        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.TRACK,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION
                },    // keys for select. null means all
                MediaStore.Audio.Media.ALBUM_ID + "=" + AlbumId,
                null,
                MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            Log.d("TRACK", "TRACK..........");
            Log.d("TRACK","ARTIST = "+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            Log.d("TRACK","TITLE = "+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            Log.d("TRACK","ALBUM_ID = "+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            Log.d("TRACK","TRACK = "+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)));
            Log.d("TRACK","DISPLAY_NAME = "+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            Log.d("TRACK","DURATION = "+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));

            try {
                TrackListData trc = new TrackListData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            android.R.drawable.ic_dialog_info,
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                myListData.add(trc);
            } catch(Exception e){
                Log.d("TRACK","cannt MediaStore.Audio.Media.DATA");
                TrackListData trc = new TrackListData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            android.R.drawable.ic_dialog_info,
                            "");
                myListData.add(trc);
            }
        }
        Log.d("TRACK", "TRACK..........");

        ContentResolver resolver2 = me.getContentResolver();
        Cursor cursor2 = resolver2.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Albums._ID,
                            MediaStore.Audio.Albums.ALBUM_ART,
                    },    // keys for select. null means all
                    MediaStore.Audio.Media._ID + "=" + AlbumId,
                    null,
                    null
        );

        while (cursor2.moveToNext()) {
            Log.d("TRACK", "ALBUM ARTWORK.....................");
            Log.d("TRACK", "ALBUM_ART = " + cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
            AlbumArtURL = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        }
        if( cursor != null ) {
            cursor.close();
        }
        Log.d("TRACK", "ALBUM ARTWORK.....................");
        refresh();
    }

    public void refresh()
    {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateTracks(myListData, AlbumArtURL);
                mAdapter.notifyDataSetChanged();
                recyclerView.invalidate();
            }
        });

    }
}