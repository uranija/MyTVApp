package com.jolita.mytvapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jolita.mytvapp.R;
import com.jolita.mytvapp.model.Video;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videoList;

    private Context mContext;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param mContext  The current context. Used to inflate the layout file.
     * @param videoList A List of Movie objects to display in a list
     */

    public VideoAdapter(Context mContext, List<Video> videoList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

        this.mContext = mContext;
        this.videoList = videoList;


    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  *                  can use this viewType integer to provide a different layout.
     * @return A new MovieViewHolder that holds the View for each list item
     */


    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.video_card, viewGroup, false);

        return new VideoAdapter.VideoViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(final VideoAdapter.VideoViewHolder viewHolder, int position) {

        viewHolder.title.setText(videoList.get(position).getName());

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return videoList.size();

    }

    /**
     * Cache of the children views for a list item.
     */


    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView title;


        public VideoViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            /**
             * Constructor for our ViewHolder. Within this constructor, we get a reference to our
             * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
             * onClick method below.
             * @param itemView The View that you inflated in
             *                 {@link GreenAdapter#onCreateViewHolder(ViewGroup, int)}
             */


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {


                        /* This is the class that we want to start (and open) when the button is clicked. */
                        //Intent intent=new Intent(mContext, DetailActivity.class);
                        /*
                         * We use the putExtra method of the Intent class to pass some extra stuff to the
                         * Activity that we are starting. Generally, this data is quite simple, such as
                         * a String or a number. However, there are ways to pass more complex objects.
                         */
                        Video clickedvideo = videoList.get(pos);
                        String trailerId = videoList.get(pos).getKey();
                        /**
                         * Instructions for building youtube url with movie database api "https://www.themoviedb.org/talk/566f816f92514173ff014fdc"
                         */
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID", trailerId);
                        mContext.startActivity(intent);


                    }


                }
            });


        }


    }
}
