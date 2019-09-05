package net.jp.garlands.simpleandroidplayer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.MyViewHolder> {
//    private String[] mDataset;
    private List<TrackListData> listdata;
    private MyViewHolder holder;
    private String AlbumArtURL;
//    private AdapterView.OnItemClickListener mListeer;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    private TrackActivity.OnItemClickListener onItemTouchListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrackAdapter(Context context,
                        int layoutId,
                        List<TrackListData> myDataset,
                        String AlbumArtURL,
                        TrackActivity.OnItemClickListener listener ) {
        this.listdata = myDataset;
        this.AlbumArtURL = AlbumArtURL;
        this.onItemTouchListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.layout_track_listitem, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, this);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder h, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder = h;
        final TrackListData mylistdata = listdata.get(position);
        holder.textView.setText(listdata.get(position).getDescription());

        try {
            URI uri = new URI(AlbumArtURL);
            holder.imageView.setImageURI(Uri.parse(uri.toString()));
        }catch(Exception e){
            Log.d("TEST",e.getLocalizedMessage());
//            holder.imageView.setImageResource(listdata.get(position).getImgId());
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+mylistdata.getDescription(),Toast.LENGTH_LONG).show();
//                int pos = view.getPosition();
                onItemTouchListener.onItemClick(view, holder.getLayoutPosition());
            }
        });

    }

    public void updateTracks(List<TrackListData> myDataset, String AlbumArtURL){
        this.listdata = myDataset;
        this.AlbumArtURL = AlbumArtURL;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        private TrackAdapter adapter;

        public MyViewHolder(View itemView, TrackAdapter adapter) {
            super(itemView);

            this.adapter = adapter;
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeTrackLayout);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    onItemTouchListener.onItemClick(v, pos);
//                }
//            });
        }
    }
}