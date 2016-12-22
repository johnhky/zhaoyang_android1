package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.PermissionsUtil;

import io.ganguo.library.Config;

/**
 * Created by lucas on 1/29/16.
 */
public class SystemMsgListActivity extends PageActivity2 {
    public static final int PHONE_CALL_REQUEST = 1;
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private PushModule api = Api.of(PushModule.class);
    private String visitTimeKey = LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT);

    private PermissionsUtil permissionsUtil;

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
        permissionsUtil = new PermissionsUtil(this);

        Config.putLong(visitTimeKey, System.currentTimeMillis());
    }


    @Override
    protected void loadMore() {
        super.loadMore();
        api.systemMsg(getCallback().getPage()).enqueue(getCallback());
    }

    public void onMenuClicked() {
        TwoChoiceDialog.show(this, "020-4008352600", "取消", "呼叫", new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(MaterialDialog dialog) {
                if (permissionsUtil.lacksPermissions(PermissionsUtil.PERMISSION_CALL)) {
                    permissionsUtil.requestPermissions(SystemMsgListActivity.this, PHONE_CALL_REQUEST, PermissionsUtil.PERMISSION_CALL);
                    return;
                }
                try {
                    Uri uri = Uri.parse("tel:4008352600");
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PHONE_CALL_REQUEST) {
            if (grantResults.length > 0 &&
                    grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                onMenuClicked();
            }
        }
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
        getMenuInflater().inflate(R.menu.menu_help_service, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call: {
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
