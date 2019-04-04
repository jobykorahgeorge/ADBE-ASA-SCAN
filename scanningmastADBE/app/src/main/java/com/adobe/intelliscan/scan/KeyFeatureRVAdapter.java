package com.adobe.intelliscan.scan;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adobe.intelliscan.R;
import com.adobe.intelliscan.model2.KeyFeature;
import com.adobe.intelliscan.model2.ProductResponse;
import com.adobe.intelliscan.models.KeyFeature1;
import com.google.android.gms.common.api.Api;

import java.lang.annotation.Target;
import java.util.List;

/**
 * Created by arun on 3/11/19.
 * Purpose -
 */
public class KeyFeatureRVAdapter extends RecyclerView.Adapter<KeyFeatureRVAdapter.ViewHolder> {

    List<KeyFeature> keyFeature;
    Context context;

    public KeyFeatureRVAdapter(Context context , List<KeyFeature> keyFeatures){
        this.keyFeature = keyFeatures;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.key_feature_recycler_item, parent, false);

        KeyFeatureRVAdapter.ViewHolder holder = new KeyFeatureRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.category.setText(keyFeature.get(position).getCategory().toUpperCase());
//        Log.d("hai","hai"+keyFeature.getCategory().toString());
//            holder.categoryDetails.setText(keyFeature.getCategoryFeatures().get());
        StringBuilder stringBuilder = new StringBuilder();
            for(int j=0;j<keyFeature.get(position).getCategoryFeatures().size();j++){
//                test = "\u2022"+test+keyFeature.get(position).getCategoryFeatures().get(j)+"\n";
                String cetegoryDtail = keyFeature.get(position).getCategoryFeatures().get(j);
                stringBuilder.append("\u2022  ");
                stringBuilder.append(cetegoryDtail);
                stringBuilder.append("\n");
            }
            holder.categoryDetails.setText(stringBuilder);
//            holder.categoryDetails.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

    }

    @Override
    public int getItemCount() {
        return keyFeature.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;
        TextView categoryDetails;
        public ViewHolder(View itemView) {
            super(itemView);

            category = (TextView) itemView.findViewById(R.id.category);
            categoryDetails = (TextView) itemView.findViewById(R.id.category_details);

//            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            categoryDetails.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
