package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.doctor.sun.event.OnTokenExpireEvent;
import com.doctor.sun.im.Messenger;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.AppManager;
import io.ganguo.library.core.event.EventHub;
import io.realm.Realm;

/**
 * Created by rick on 11/27/15.
 */
public abstract class BaseFragmentActivity2 extends AppCompatActivity implements HeaderViewModel.HeaderView {
    protected String TAG = getClass().getSimpleName();

    private Realm realm;
    private OnTokenExpireEvent tokenExpire;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Messenger.getInstance().login();
        // register
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
    public void onFirstMenuClicked() {
    }

    @Override
    public void onMenuClicked() {
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
}
