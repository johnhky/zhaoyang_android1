package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityRegisterBinding;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.fragment.RegisterFragment;
import com.doctor.sun.ui.handler.RegisterHandler;


/**
 * Created by rick on 11/17/15.
 */
public class RegisterActivity extends BaseFragmentActivity2 {

    private ActivityRegisterBinding binding;
    private RegisterHandler handler;

    public static Intent makeIntent(Context context, int data) {
//        Intent i = new Intent(context, RegisterActivity.class);
//        i.putExtra(Constants.DATA, data);
//        return i;
        return RegisterFragment.intentFor(context);
    }


    private int getData() {
        return getIntent().getIntExtra(Constants.DATA, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        int type = getData();
        handler = new RegisterHandler(type);
        switch (type) {
            case AuthModule.FORGOT_PASSWORD: {
                binding.llyPolicy.setVisibility(View.GONE);
                binding.llyRegisterType.setVisibility(View.GONE);
                handler.setEnable(true);
                break;
            }
            default:
        }
        handler.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                switch (i) {
                    case BR.enable: {
                        supportInvalidateOptionsMenu();
                    }
                }
            }
        });
        binding.setHandler(handler);
    }

    public void onMenuClicked() {
        int type = getData();
        if (type == AuthModule.FORGOT_PASSWORD) {
            handler.resetPassword(null);
        } else {
            handler.register(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (getData()) {
            case AuthModule.DOCTOR_TYPE: {
                getMenuInflater().inflate(R.menu.menu_next, menu);
                break;
            }
            case AuthModule.PATIENT_TYPE: {
                getMenuInflater().inflate(R.menu.menu_next, menu);
                break;
            }
            case AuthModule.FORGOT_PASSWORD: {
                getMenuInflater().inflate(R.menu.menu_confirm, menu);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
            case R.id.action_confirm: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (handler.isEnable()) {
            switch (getData()) {
                case AuthModule.PATIENT_TYPE:
                case AuthModule.DOCTOR_TYPE: {
                    getMenuInflater().inflate(R.menu.menu_next, menu);
                    break;
                }
                case AuthModule.FORGOT_PASSWORD: {
                    getMenuInflater().inflate(R.menu.menu_confirm, menu);
                    break;
                }
            }
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public String getMidTitleString() {
        switch (getData()) {
            case AuthModule.DOCTOR_TYPE: {
                return "注册";
            }
            case AuthModule.PATIENT_TYPE: {
                return "注册";
            }
            case AuthModule.FORGOT_PASSWORD: {
                return "重置密码";
            }
            default:
                return "";
        }
    }
}
