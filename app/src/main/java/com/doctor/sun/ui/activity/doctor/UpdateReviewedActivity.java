package com.doctor.sun.ui.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.doctor.sun.R;
import com.doctor.sun.event.MainTabChangedEvent;

/**
 * Created by test on 17/4/12.
 */

public class UpdateReviewedActivity extends BaseDoctorActivity{

    private Button p_reviewed_update,p_reviewed_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.p_activity_reviewed);
            initView();
    }

    private void initView(){
        p_reviewed_back  = (Button) findViewById(R.id.p_reviewed_back);
        p_reviewed_update = (Button) findViewById(R.id.p_reviewed_update);
        p_reviewed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMain = new Intent();
                toMain.setClass(UpdateReviewedActivity.this,MainActivity.class);
                startActivity(toMain);
                finish();
            }
        });
        p_reviewed_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onMainTabChangedEvent(MainTabChangedEvent e) {
        super.onMainTabChangedEvent(e);
    }
}
