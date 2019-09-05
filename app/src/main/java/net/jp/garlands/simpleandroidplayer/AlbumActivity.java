package net.jp.garlands.simpleandroidplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    GridView gridview;

    private static Activity me = null;
    private List<AlbumListData> albums = new ArrayList<>();

    private static String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private List<Integer> imgList = new ArrayList<>();
    private AlbumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        me = this;

        if( checkPermission() == true ) {
            getAlbumList();
        }
        gridview = findViewById(R.id.album_grid_view);
        adapter = new AlbumAdapter(this.getApplicationContext(),
                R.layout.layout_album_listitem,
                albums
        );

        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                AlbumListData album = albums.get(position);

                Intent intent = new Intent(AlbumActivity.this, TrackActivity.class);
                intent.putExtra("com.example.simpleandroidplayer.albumsID", album.getAlbumId() );
                startActivityForResult(intent, 0);
            }
        });
        gridview.requestFocus();
    }

    private static boolean checkPermission() {
        Log.i("ALBUM", "Enter checkPermission()");
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
                    getAlbumList();
                }
                return;
            }
        }
    }

    private void getAlbumList(){
        Log.i("ALBUM", "Enter getAlbumList()");
        ContentResolver resolver = me.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Albums.ALBUM,
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM_ART,
                        MediaStore.Audio.Albums.ARTIST,
                        MediaStore.Audio.Albums.NUMBER_OF_SONGS
                },    // keys for select. null means all
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
                Log.d("ALBUM", ".....................");
                Log.d("ALBUM", "ALBUM = " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
                Log.d("ALBUM", "_ID = " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
                Log.d("ALBUM", "ALBUM_ART = " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                Log.d("ALBUM", "ARTIST = " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
                Log.d("ALBUM", "NUMBER_OF_SONGS = " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)));
                AlbumListData album = new AlbumListData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
                albums.add(album);
        }
        if( cursor != null ) {
            cursor.close();
        }
        Log.d("ALBUM", ".....................");

        refresh();
    }

    public void refresh()
    {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.updateItems(albums);
                adapter.notifyDataSetChanged();
                gridview.invalidate();
            }
        });

    }
}