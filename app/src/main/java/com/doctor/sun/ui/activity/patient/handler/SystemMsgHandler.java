package com.doctor.sun.ui.activity.patient.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.ui.activity.SystemMsgListActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.ganguo.library.Config;

/**
 * Created by lucas on 1/29/16.
 */
public class SystemMsgHandler {
    public static final String TAG = SystemMsgHandler.class.getSimpleName();
    private SystemMsg data;

    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private String visitTimeKey = LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT);

    public SystemMsgHandler(SystemMsg systemTip) {
        data = systemTip;
    }

    public void setTime(SimpleAdapter adapter, SystemMsg msg) {
        if (Config.getLong(visitTimeKey, 0) < msg.getCreated()) {
            Config.putLong(visitTimeKey, msg.getCreated() + 1);

            adapter.putLong(AdapterConfigKey.LAST_VISIT_TIME, msg.getCreated());
            msg.notifyChange();
        }
    }

    public void systemMsgList(Context context, int count) {
        Intent intent = SystemMsgListActivity.makeIntent(context, count);
        data.reset();
        context.startActivity(intent);
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

    public int isViewVisible(SimpleAdapter adapter) {
        long lastVisitTime = adapter.getLong(AdapterConfigKey.LAST_VISIT_TIME);

        if (haveRead(lastVisitTime)) {
            return View.GONE;
        } else {
            return View.VISIBLE;
        }
    }
}
