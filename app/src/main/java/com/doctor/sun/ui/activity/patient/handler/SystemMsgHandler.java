package com.doctor.sun.ui.activity.patient.handler;

import android.content.Intent;

import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.ui.activity.patient.SystemMsgListActivity;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lucas on 1/29/16.
 */
public class SystemMsgHandler {
    public static final String TAG = SystemMsgHandler.class.getSimpleName();
    private SystemMsg data;

    public SystemMsgHandler(SystemMsg systemTip) {
        data = systemTip;
    }


    public void systemMsgList(BaseAdapter adapter) {
        Intent intent = SystemMsgListActivity.makeIntent(adapter.getContext(), adapter.getItemCount() - 2);
        adapter.getContext().startActivity(intent);
    }

    public boolean haveRead(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date parse;
        try {
            parse = format.parse(data.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
        return parse.getTime() < time;
    }
}
