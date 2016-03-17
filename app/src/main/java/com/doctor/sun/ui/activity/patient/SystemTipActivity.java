package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.activity.PageActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.SystemTipAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.doctor.sun.util.PermissionsUtil;

import io.ganguo.library.Config;

/**
 * Created by lucas on 1/29/16.
 */
public class SystemTipActivity extends PageActivity {
    public static final int PHONE_CALL_REQUEST = 1;
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private PushModule api = Api.of(PushModule.class);
    private String visitTimeKey = LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT);

    private PermissionsUtil permissionsUtil;

    public static Intent makeIntent(Context context, int appointmentNumber) {
        Intent i = new Intent(context, SystemTipActivity.class);
        i.putExtra(Constants.NUMBER, appointmentNumber);
        return i;
    }

    private int getAppointmentNumber() {
        return getIntent().getIntExtra(Constants.NUMBER, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAdapter().mapLayout(R.layout.p_item_system_tip, R.layout.item_system_tip);
        permissionsUtil = new PermissionsUtil(this);

        Config.putLong(visitTimeKey, System.currentTimeMillis());
    }

    @Override
    protected void loadMore() {
        api.systemTip(getCallback().getPage()).enqueue(getCallback());
    }

    @NonNull
    @Override
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
        TwoSelectorDialog.showTwoSelectorDialog(this, "020-81212600", "取消", "呼叫", new TwoSelectorDialog.GetActionButton() {
            @Override
            public void onClickPositiveButton(TwoSelectorDialog dialog) {
                if (permissionsUtil.lacksPermissions(PermissionsUtil.PERMISSION_CALL)) {
                    permissionsUtil.requestPermissions(SystemTipActivity.this, PHONE_CALL_REQUEST, PermissionsUtil.PERMISSION_CALL);
                    return;
                }
                try {
                    Uri uri = Uri.parse("tel:02081212600");
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onClickNegativeButton(TwoSelectorDialog dialog) {
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
    protected SimpleAdapter createAdapter() {
        return new SystemTipAdapter(this, Config.getLong(visitTimeKey, -1));
    }
}
