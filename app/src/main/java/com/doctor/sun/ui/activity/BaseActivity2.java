package com.doctor.sun.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.doctor.sun.event.OnTokenExpireEvent;
import com.doctor.sun.im.Messenger;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.AppManager;
import io.ganguo.library.core.event.EventHub;
import io.realm.Realm;


/**
 * Created by rick on 10/15/15.
 */
public abstract class BaseActivity2 extends Activity implements HeaderViewModel.HeaderView {
    protected String TAG = getClass().getSimpleName();

    protected Realm realm;
    private OnTokenExpireEvent tokenExpire;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register
        if (!Messenger.getInstance().isLogin()) {
            Messenger.getInstance().login();
        }
        AppManager.addActivity(this);
        EventHub.register(this);
        tokenExpire = new OnTokenExpireEvent(this);
        EventHub.register(tokenExpire);
        realm = Realm.getDefaultInstance();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unregister
        EventHub.unregister(this);
        EventHub.unregister(tokenExpire);
        AppManager.removeActivity(this);
        realm.close();
    }

    @Override
    public void onBackClicked() {
        finish();
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

}
