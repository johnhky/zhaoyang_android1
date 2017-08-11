package com.doctor.sun.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.event.ReadMessageEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.squareup.otto.Subscribe;

import io.ganguo.library.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MESSAGE_UPDATE);
        registerReceiver(receiver, filter);
        Config.putLong(visitTimeKey, System.currentTimeMillis());
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getAdapter().clear();
        getCallback().resetPage();
        api.systemMsg(getCallback().getPage()).enqueue(getCallback());
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.systemMsg(getCallback().getPage()).enqueue(getCallback());

    }

    public void onMenuClicked() {
        api.markMessageAsRead("all").enqueue(new Callback<ApiDTO>() {
            @Override
            public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                if (response.body().getStatus().equals("200")) {
                    Toast.makeText(SystemMsgListActivity.this,
                            "全部未读消息标记为已读", Toast.LENGTH_SHORT).show();
                    getAdapter().clear();
                    getCallback().resetPage();
                    api.systemMsg(getCallback().getPage()).enqueue(getCallback());
                }
            }

            @Override
            public void onFailure(Call<ApiDTO> call, Throwable t) {

            }
        });
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

    @Subscribe
    public void onReadMessageEvent(ReadMessageEvent event) {
        getAdapter().clear();
        getCallback().resetPage();
        api.systemMsg(getCallback().getPage()).enqueue(getCallback());
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.MESSAGE_UPDATE)) {
                getAdapter().clear();
                getCallback().resetPage();
                api.systemMsg(getCallback().getPage()).enqueue(getCallback());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
