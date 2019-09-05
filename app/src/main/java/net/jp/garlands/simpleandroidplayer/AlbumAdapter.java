package net.jp.garlands.simpleandroidplayer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends BaseAdapter {

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    private List<Integer> imageList = new ArrayList<>();
    private List<AlbumListData> albumlist = new ArrayList<>();

    private List<String> names;
    private LayoutInflater inflater;
    private int layoutId;

    AlbumAdapter(Context context,
                int layoutId,
                List<AlbumListData> albums){

        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        albumlist = albums;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textView = convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        AlbumListData album = albumlist.get(position);

        try {
            String str =  album.getAlbumArtURL();
            URI uri = new URI(str);
            holder.imageView.setImageURI(Uri.parse(uri.toString()));
        }catch(Exception e){
            Log.d("TEST",e.getLocalizedMessage());
        }
        holder.textView.setText(album.getAlbum());
        return convertView;
    }

    @Override
    public int getCount() {
        return albumlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateItems(List<AlbumListData> albums){
        albumlist = albums;
    }
}