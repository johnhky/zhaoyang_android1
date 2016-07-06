package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.SystemMsgAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
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

    private int getAppointmentNumber() {
        return getIntent().getIntExtra(Constants.NUMBER, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAdapter().mapLayout(R.layout.p_item_system_msg, R.layout.item_system_msg);
        permissionsUtil = new PermissionsUtil(this);

        Config.putLong(visitTimeKey, System.currentTimeMillis());
        SystemMsg.reset();
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        getBinding().setHeader(getHeaderViewModel());
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.systemMsg(getCallback().getPage()).enqueue(getCallback());
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        int appointmentNumber = 0;
        HeaderViewModel header = new HeaderViewModel(this);
        header.setRightTitle("联系客服");
        if (Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE) {
            appointmentNumber = getAppointmentNumber();
        } else {
            appointmentNumber = getAppointmentNumber() + 1;
        }
        header.setLeftTitle("就诊(" + appointmentNumber + ")");
        return header;
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
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
        return new SystemMsgAdapter(this, Config.getLong(visitTimeKey, -1));
    }

}
