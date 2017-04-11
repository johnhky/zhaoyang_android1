package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityAdviceBinding;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

/**
 * Created by lucas on 1/15/16.
 * 意见反馈
 */
public class AdviceActivity extends BaseFragmentActivity2 {
    private ActivityAdviceBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_advice;
    }

    public void onMenuClicked() {
        api.sendAdvice(binding.etFeedback.getText().toString()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(AdviceActivity.this, "系统已经收到您的反馈,感谢您!", Toast.LENGTH_SHORT).show();
                finish();
//                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AdviceActivity.class);
        return i;
    }

}