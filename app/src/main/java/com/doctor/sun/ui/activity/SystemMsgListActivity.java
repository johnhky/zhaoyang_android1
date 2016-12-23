package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;

import io.ganguo.library.Config;

/**
 * Created by lucas on 1/29/16.
 */
public class SystemMsgListActivity extends PageActivity2 {
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private String visitTimeKey = LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT);
    private PushModule api = Api.of(PushModule.class);

    public static Intent makeIntent(Context context, int appointmentNumber) {
        Intent i = new Intent(context, SystemMsgListActivity.class);
        i.putExtra(Constants.NUMBER, appointmentNumber);
        return i;
    }

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, SystemMsgListActivity.class);

        return i;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAdapter().mapLayout(R.layout.p_item_system_msg, R.layout.item_system_msg);

        Config.putLong(visitTimeKey, System.currentTimeMillis());
    }


    @Override
    protected void loadMore() {
        super.loadMore();
        api.systemMsg(getCallback().getPage()).enqueue(getCallback());
    }

    public void onMenuClicked() {

    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = new SimpleAdapter();
        adapter.putLong(AdapterConfigKey.LAST_VISIT_TIME, Config.getLong(visitTimeKey, -1));

        return adapter;
    }


    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何系统消息";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mark_as_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mark_as_read: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_system_msg;
    }
}
