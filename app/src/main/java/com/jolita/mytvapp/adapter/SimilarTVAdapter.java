package com.jolita.mytvapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jolita.mytvapp.R;
import com.jolita.mytvapp.model.SimilarTV;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SimilarTVAdapter extends RecyclerView.Adapter<SimilarTVAdapter.SimilarTVViewHolder> {
    private List<SimilarTV> similarTVList;

    private Context mContext;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param mContext  The current context. Used to inflate the layout file.
     * @param similarTVList A List of Movie objects to display in a list
     */

    public SimilarTVAdapter(Context mContext, List<SimilarTV> similarTVList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

        this.mContext = mContext;
        this.similarTVList = similarTVList;


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
    public SimilarTVAdapter.SimilarTVViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.similartv_card, viewGroup, false);

        return new SimilarTVViewHolder(view);
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
    public void onBindViewHolder(final SimilarTVAdapter.SimilarTVViewHolder viewHolder, int position) {
        viewHolder.title.setText(similarTVList.get(position).getOriginalTitle());

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + similarTVList.get(position).getPosterPath())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.load)
                .into(viewHolder.thumbnail);
    }


    public void setSimilarTV(List<SimilarTV> similartv) {
        similarTVList = similartv;
        notifyDataSetChanged();
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return similarTVList.size();

    }

    /**
     * Cache of the children views for a list item.
     */


    public class SimilarTVViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView title;
        public TextView userrating;

        public SimilarTVViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title=(TextView) itemView.findViewById(R.id.title);
            userrating = (TextView) itemView.findViewById(R.id.userrating);

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


                        SimilarTV clickedTVCard = similarTVList.get(pos);



                    }


                }
            });


        }


    }


}



