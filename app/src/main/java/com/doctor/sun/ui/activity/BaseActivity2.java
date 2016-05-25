package com.doctor.sun.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;

import com.doctor.sun.event.OnTokenExpireEvent;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.AppManager;
import io.ganguo.library.core.event.EventHub;
import io.realm.Realm;


/**
 * Created by rick on 10/15/15.
 */
public abstract class BaseActivity2 extends Activity implements HeaderViewModel.HeaderView {
    protected String TAG = getClass().getSimpleName();

    private Realm realm;
    private OnTokenExpireEvent tokenExpire;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register
        if (!IMManager.getInstance().isNIMLogin()) {
            IMManager.getInstance().login();
        }
        AppManager.addActivity(this);
        EventHub.register(this);
        tokenExpire = new OnTokenExpireEvent(this);
        EventHub.register(tokenExpire);
        realm = getRealm();


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

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onTitleClicked() {

    }

    @Override
    public void onMenuClicked() {
    }

    @Override
    public void onFirstMenuClicked() {

    }

    public final String getText(EditText editText) {
        return editText.getText().toString();
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
}
