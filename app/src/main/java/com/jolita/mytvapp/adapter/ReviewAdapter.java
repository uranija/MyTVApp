package com.jolita.mytvapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jolita.mytvapp.R;
import com.jolita.mytvapp.model.Review;
import com.jolita.mytvapp.widget.UpdateTVService;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>  {
    private List<Review> reviewList;

    private Context mContext;

    private ArrayList<String> tvStrArray;


    {
        reviewList = new ArrayList<>();
        tvStrArray = new ArrayList<>();

    }

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param mContext  The current context. Used to inflate the layout file.
     * @param reviewList A List of Movie objects to display in a list
     */

    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

        this.mContext = mContext;
        this.reviewList = reviewList;


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
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_card, viewGroup, false);

        return new ReviewAdapter.ReviewViewHolder(view);
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
    public void onBindViewHolder(final ReviewAdapter.ReviewViewHolder viewHolder, int position) {

        viewHolder.author.setText(reviewList.get(position).getAuthor());
        viewHolder.content.setText(reviewList.get(position).getContent());

        tvStrArray.add(reviewList.get(position).getAuthor());
        tvStrArray.add(reviewList.get(position).getContent());

        UpdateTVService.startBakingService(mContext, tvStrArray);


    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return reviewList.size();

    }

    /**
     * Cache of the children views for a list item.
     */


    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView author; TextView content;


        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);

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
                        Review clickedReview = reviewList.get(pos);
                        String review = reviewList.get(pos).getAuthor();







                    }


                }
            });


        }


    }
}
