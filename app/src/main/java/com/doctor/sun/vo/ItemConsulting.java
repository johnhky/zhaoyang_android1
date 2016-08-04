package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.ItemHelper;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.HashMap;

/**
 * Created by rick on 5/7/2016.
 */

public class ItemConsulting implements SortedItem {
    private Appointment data;
    private RecentContact tid;

    public ItemConsulting(RecentContact tid, Appointment data) {
        this.tid = tid;
        this.data = data;
    }

    public Appointment getData() {
        return data;
    }

    public void setData(Appointment data) {
        this.data = data;
    }

    public RecentContact getTid() {
        return tid;
    }

    public void setTid(RecentContact tid) {
        this.tid = tid;
    }

    @Override
    public int getLayoutId() {
        if (Settings.isDoctor()) {
            return R.layout.item_consulting2;
        } else {
            return R.layout.p_item_consulting2;
        }
    }

    @Override
    public long getCreated() {
        return tid.getTime();
    }

    @Override
    public String getKey() {
        return String.valueOf(data.getTid());
    }

    @Override
    public int getSpan() {
        return 12;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }

    public Doctor getDoctor() {
        return data.getDoctor();
    }

    public String getProgress() {
        return data.getProgress();
    }

    public String getAvatar() {
        return data.getAvatar();
    }

    public AppointmentHandler getHandler() {
        return data.getHandler();
    }

    public void chat(Context context, SortedListAdapter adapter, BaseViewHolder vh) {
        if (data.getTid() != 0) {
            Intent intent = ChattingActivity.makeIntent(context, data);
            intent.putExtra(ItemHelper.HANDLER, messenger(adapter, vh));
            context.startActivity(intent);
        } else {
            if (BuildConfig.DEV_MODE) {
                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Messenger messenger(final SortedListAdapter mAdapter, final BaseViewHolder vh) {
        return new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mAdapter.insert(new ItemConsulting(tid, (Appointment) msg.obj));
                return false;
            }
        }));
    }
}
