package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.handler.TimeHandler;

import java.util.List;

/**
 * Created by heky on 17/5/23.
 */

public class TimeAdapter extends BaseAdapter {

    private List<Time> list;
    private Context context;
    private View view;
    private int index = 0;

    public TimeAdapter(Context context, List<Time> list,View view) {
        this.context = context;
        this.list = list;
        this.view = view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_doctor_time, null);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TimeHandler handler = new TimeHandler(list.get(position));
        holder.tv_time.setText(list.get(position).getFrom() + ":" + list.get(position).getTo());
        if (list.get(position).getType() == 4) {
            holder.tv_type.setText("诊所面诊:"+handler.getWeekLabel());
        }else{
            holder.tv_type.setText("专属网诊"+handler.getWeekLabel());
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_type, tv_time;
    }
}
