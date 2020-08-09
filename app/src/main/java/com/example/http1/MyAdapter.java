package com.example.http1;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    List<LessionResult.Lesson> mData=new ArrayList<>();
    private Context mContext;

    public MyAdapter(List<LessionResult.Lesson> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list,viewGroup,false);
            holder = new ViewHolder();
            holder.img = (SimpleDraweeView) view.findViewById(R.id.img);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.learner=(TextView) view.findViewById(R.id.learner);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Uri uri = Uri.parse(mData.get(i).getPicSmall());
        holder.img.setImageURI(uri);

        holder.name.setText(mData.get(i).getName());
        holder.learner.setText(""+mData.get(i).getLearner());
        return view;
    }
    private class ViewHolder{
        SimpleDraweeView img;
        TextView name;
        TextView learner;
    }


}
