package com.example.phuwarin.facebookfriend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Phuwarin on 6/8/2016.
 */
public class BufferList extends BaseAdapter {

    private String[] friendName;
    private String[] friendPic;
    private Context context;

    public BufferList(Context context, String[] friendName, String[] friendPic) {
        this.context = context;
        this.friendName = friendName;
        this.friendPic = friendPic;
    }

    @Override
    public int getCount() {
        return friendName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_listview, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.label);
        textView.setText(friendName[position]);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.picLabel);
        Picasso.with(context).load(friendPic[position]).into(imageView);

        return convertView;
    }
}
