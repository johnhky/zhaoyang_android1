package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.event.OnTokenExpireEvent;
import com.doctor.sun.im.IMManager;

import io.ganguo.library.AppManager;
import io.ganguo.library.core.event.EventHub;
import io.realm.Realm;

/**
 * Created by rick on 11/27/15.
 */
public abstract class BaseFragmentActivity2 extends UMBaseFragmentActivity {
    protected String TAG = getClass().getSimpleName();

    private Realm realm;
    private OnTokenExpireEvent tokenExpire;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!IMManager.getInstance().isNIMLogin()) {
            IMManager.getInstance().login();
        }
        // register
        AppManager.addActivity(this);
        EventHub.register(this);
        tokenExpire = new OnTokenExpireEvent(this);
        EventHub.register(tokenExpire);
        realm = getRealm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            toolbar.setTitle("");
            toolbar.setSubtitle("");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isTaskRoot()) {
                        onBackPressed();
                    }
                }
            });
        }
        TextView midTitle = (TextView) findViewById(R.id.tb_title);
        if (midTitle != null) {
            midTitle.setText(getMidTitleString());
        }
    }


    protected Realm getRealm() {
        if (realm == null || realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unregister
        EventHub.unregister(this);
        EventHub.unregister(tokenExpire);
        AppManager.removeActivity(this);
        getRealm().close();
    }

    public final String getText(EditText editText) {
        return editText.getText().toString();
    }

    protected void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        overridePendingTransition(0, 0);
    }

    protected boolean shouldCheck() {
        return true;
    }

    public boolean getBooleanExtra(String name, boolean defaultValue) {
        return getIntent().getBooleanExtra(name, defaultValue);
    }

    public long getLongExtra(String name, long defaultValue) {
        return getIntent().getLongExtra(name, defaultValue);
    }

    public float getFloatExtra(String name, float defaultValue) {
        return getIntent().getFloatExtra(name, defaultValue);
    }

    public double getDoubleExtra(String name, double defaultValue) {
        return getIntent().getDoubleExtra(name, defaultValue);
    }

    public String getStringExtra(String name) {
        return getIntent().getStringExtra(name);
    }

    public <T extends Parcelable> T getParcelableExtra(String name) {
        return getIntent().getParcelableExtra(name);
    }

    public String getMidTitleString() {
        return getString(getMidTitle());
    }

    public int getMidTitle() {
        return R.string.title;
    }

    public String getSubTitle() {
        return "";
    }
}
