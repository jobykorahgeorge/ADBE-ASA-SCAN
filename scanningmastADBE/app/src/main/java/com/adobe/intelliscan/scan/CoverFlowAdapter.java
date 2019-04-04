package com.adobe.intelliscan.scan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.adobe.intelliscan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arun on 3/12/19.
 * Purpose -
 */
public class CoverFlowAdapter extends BaseAdapter {

    private ArrayList<String> data;
    private Context context;

    public CoverFlowAdapter(Context context, ArrayList<String> imageurl) {
        this.context = context;
        this.data = imageurl;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.image_set_adapter_item, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(data.get(position)).into(viewHolder.gameImage);



        return convertView;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        };
    }

    private static class ViewHolder {

        private ImageView gameImage;

        public ViewHolder(View v) {

            gameImage = (ImageView) v.findViewById(R.id.image);

        }
    }
}
