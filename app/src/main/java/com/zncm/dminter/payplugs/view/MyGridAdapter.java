package com.zncm.dminter.payplugs.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zncm.dminter.payplugs.R;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.CardInfo;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.Xutils;

import java.util.List;


public class MyGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CardInfo> lists;
    private Activity ctx;


    public MyGridAdapter(Activity ctx) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    public void setItem(List<CardInfo> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (Xutils.listNotNull(lists)) {
            return lists.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = this.inflater.inflate(R.layout.bottom_item, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        setData(position, holder);
        return convertView;
    }

    public void setData(final int position, final MyViewHolder holder) {
        if (lists != null) {
            final CardInfo data = (CardInfo) lists.get(position);
            if (data == null) {
                return;
            }
            if (Xutils.isNotEmptyOrNull(data.getTitle())) {
                holder.text.setVisibility(View.VISIBLE);
                holder.text.setText(data.getTitle());
            } else {
                holder.text.setVisibility(View.GONE);
            }

            if (Xutils.isNotEmptyOrNull(data.getIconUrl())) {
//                holder.image.setVisibility(View.VISIBLE);
                Glide.with(ctx).load(data.getIconUrl()).into(holder.image);
            } else {
//                holder.image.setVisibility(View.GONE);
                if (data.getIconRes() != 0) {
                    holder.image.setImageResource(data.getIconRes());
                } else {
                    holder.image.setImageResource(R.drawable.ic_launcher);
                }
            }
        }
    }

    public class MyViewHolder {
        public TextView text;
        public ImageView image;
    }

}
