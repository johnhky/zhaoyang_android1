package com.doctor.sun.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorDetail2Binding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.NewDoctor;

import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.patient.POrderMessageActivity;
/**
 * Created by kb on 13/12/2016.
 */

public class DoctorDetailActivity2 extends BaseFragmentActivity2 {

    private ActivityDoctorDetail2Binding binding;

    private ProfileModule api = Api.of(ProfileModule.class);


    private Doctor doctor;
    private int id;
    public static Intent makeIntent(Context context, Doctor data) {
        Intent i = new Intent(context, DoctorDetailActivity2.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    public static Intent makeIntent(Context context, NewDoctor data) {
        Intent i = new Intent(context, DoctorDetailActivity2.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail2);
        IntentFilter filter = new IntentFilter();
        filter.addAction("unconllect");
        filter.addAction("conllect");
        registerReceiver(receiver, filter);
        showDoctorInfo();
        postponeTransition();
        initWidget();
    }

    private void showDoctorInfo() {
        ToolModule api = Api.of(ToolModule.class);
        api.doctorInfo(getData().getId()).enqueue(new SimpleCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                binding.setData(response);
                Log.e("eeee", binding.getData().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    public void initView() {
        api.comments(getData().getId(), "1").enqueue(new SimpleCallback<PageDTO<Comment>>() {
            @Override
            protected void handleResponse(PageDTO<Comment> response) {
                binding.tvComment.setText("评论(" + response.getData().size() + ")");
            }
        });
        api.articles(getData().getId(), "1").enqueue(new SimpleCallback<PageDTO<Article>>() {
            @Override
            protected void handleResponse(PageDTO<Article> response) {
                binding.tvArticle.setText("文章(" + response.getData().size() + ")");
            }
        });
        if ("1".equals(getData().getIsFav())) {
            binding.tvConllect.setBackgroundResource(R.drawable.ic_conllect);
            binding.tvConllect.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.tvConllect.setText("已收藏");
        } else {
            binding.tvConllect.setBackgroundResource(R.drawable.ic_unconllect);
            binding.tvConllect.setTextColor(getResources().getColor(R.color.text_color_gray));
            binding.tvConllect.setText("未收藏");
        }
    }

    public void initWidget() {
        if (getData().getSpecialistCateg()==1){
            binding.radSimple.setEnabled(false);
        }
        binding.radNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 1;
                binding.radNet.setBackgroundResource(R.drawable.ic_type_checked);
                binding.radSimple.setBackgroundResource(R.drawable.ic_type_unchecked);
                binding.radSurface.setBackgroundResource(R.drawable.ic_type_unchecked);
                binding.tvSimple.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSimple2.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvNet.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvNet2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvSurface.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSurface2.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvMoney.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvSimpleMoney.setTextColor(getResources().getColor(R.color.red_f7));
                binding.tvSurfaceMoney.setTextColor(getResources().getColor(R.color.red_f7));
                binding.tvNetRecommond.setVisibility(View.VISIBLE);
                binding.tvSimpleRecommond.setVisibility(View.GONE);
                binding.tvSurfaceRecommond.setVisibility(View.GONE);
            }
        });
        binding.radSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 2;
                binding.radSimple.setBackgroundResource(R.drawable.ic_type_checked);
                binding.radNet.setBackgroundResource(R.drawable.ic_type_unchecked);
                binding.radSurface.setBackgroundResource(R.drawable.ic_type_unchecked);
                binding.tvNet.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvNet2.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSimple.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvSimple2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvSurface.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSurface2.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSimpleMoney.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvMoney.setTextColor(getResources().getColor(R.color.red_f7));
                binding.tvSurfaceMoney.setTextColor(getResources().getColor(R.color.red_f7));
                binding.tvSimpleRecommond.setVisibility(View.VISIBLE);
                binding.tvNetRecommond.setVisibility(View.GONE);
                binding.tvSurfaceRecommond.setVisibility(View.GONE);
            }
        });
        binding.radSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 3;
                binding.radSurface.setBackgroundResource(R.drawable.ic_type_checked);
                binding.radSimple.setBackgroundResource(R.drawable.ic_type_unchecked);
                binding.radNet.setBackgroundResource(R.drawable.ic_type_unchecked);
                binding.tvSimple.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSimple2.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSurface.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvSurface2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvNet.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvNet2.setTextColor(getResources().getColor(R.color.text_color_black));
                binding.tvSurfaceMoney.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvMoney.setTextColor(getResources().getColor(R.color.red_f7));
                binding.tvSimpleMoney.setTextColor(getResources().getColor(R.color.red_f7));
                binding.tvSurfaceRecommond.setVisibility(View.VISIBLE);
                binding.tvSimpleRecommond.setVisibility(View.GONE);
                binding.tvNetRecommond.setVisibility(View.GONE);
            }
        });
        binding.flNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id==0){
                    Toast.makeText(DoctorDetailActivity2.this,"请选择就诊类型!",Toast.LENGTH_LONG).show();
                }else{
                    toOrderMessage(DoctorDetailActivity2.this);
                }

            }
        });
    }

    public void toOrderMessage(Context context) {
        Intent toOrder = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA,getData());
        toOrder.putExtra(Constants.REMARK,id);
        toOrder.putExtras(bundle);
        toOrder.putExtra(Constants.ADDRESS,binding.tvClinicAddress.getText().toString());
        toOrder.setClass(context, POrderMessageActivity.class);
        context.startActivity(toOrder);
    }

    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private void postponeTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        } else {
            supportPostponeEnterTransition();
        }
    }

    private void startPostponedTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        } else {
            supportStartPostponedEnterTransition();
        }
    }


    @Override
    public int getMidTitle() {
        return R.string.doctor_detail;
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("unconllect")) {
                binding.tvConllect.setBackgroundResource(R.drawable.ic_unconllect);
                binding.tvConllect.setTextColor(getResources().getColor(R.color.text_color_gray));
                binding.tvConllect.setText("未收藏");
            } else if (intent.getAction().equals("conllect")) {
                binding.tvConllect.setBackgroundResource(R.drawable.ic_conllect);
                binding.tvConllect.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvConllect.setText("已收藏");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
