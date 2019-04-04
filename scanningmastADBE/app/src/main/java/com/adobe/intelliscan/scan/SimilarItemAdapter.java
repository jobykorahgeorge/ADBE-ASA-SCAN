package com.adobe.intelliscan.scan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adobe.intelliscan.R;
import com.adobe.intelliscan.models.SimilarProduct;
import com.adobe.intelliscan.utils.Preferences;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arun on 2/28/19.
 * Purpose -
 */
public class SimilarItemAdapter extends RecyclerView.Adapter<SimilarItemAdapter.SimilarViewHolder> {

    private Context context;
    private List<SimilarProduct> similarProducts;

    BrowserActivityCallBack browserActivityCallBack;
    Preferences preferences ;
    String url ;

    SimilarItemAdapter(Context context,List<SimilarProduct> similarProducts){
        this.context = context;
        this.similarProducts=similarProducts;
        this.preferences = new Preferences(context);
        this.url = preferences.getData(Preferences.KEY_APP_SERVER_URL);
    }



    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarViewHolder(LayoutInflater.from(context).inflate(R.layout.similar_product_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SimilarItemAdapter.SimilarViewHolder holder, final int position) {

        holder.title.setText(similarProducts.get(position).getTitle());
        holder.price.setText(similarProducts.get(position).getPrice());
        Picasso.with(context).load(url+similarProducts.get(position).getImage()).placeholder(R.drawable.progress_animation).into(holder.imageView);


        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(similarProducts.get(position).getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);

            }
        });

        if(position==similarProducts.size()-1){
            holder.divider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return similarProducts.size();
    }


    class SimilarViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        TextView title;
        TextView price;
        ImageView imageView;
        TextView divider;


        public SimilarViewHolder(View itemView) {
            super(itemView);
            mView =itemView;
            title = mView.findViewById(R.id.similar_title);
            price = mView.findViewById(R.id.similar_price);
            imageView = mView.findViewById(R.id.similar_image);
            divider = mView.findViewById(R.id.divider);

        }
    }
}
