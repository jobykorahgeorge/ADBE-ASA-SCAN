package com.adobe.intelliscan.scan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adobe.intelliscan.R;
import com.adobe.intelliscan.model2.Review;

import java.util.List;

/**
 * Created by arun on 3/12/19.
 * Purpose -
 */
public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewViewHolder> {

    Context context;
    List<Review> reviews;

    ReviewRecyclerAdapter(List<Review> reviews,Context context){
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewRecyclerAdapter.ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.review_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        holder.userName.setText(reviews.get(position).getUserName());
        holder.date.setText(reviews.get(position).getPostDate());
        holder.userReview.setText(reviews.get(position).getUserReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewViewHolder  extends RecyclerView.ViewHolder{

        public final View mView;

        TextView userName;
        TextView date;
        TextView userReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            userName = itemView.findViewById(R.id.userName);
            date = itemView.findViewById(R.id.dateReview);
            userReview= itemView.findViewById(R.id.reviewByUser);

        }
    }
}
