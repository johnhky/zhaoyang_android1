package com.doctor.sun.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.repacked.org.antlr.v4.Tool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorDetail2Binding;
import com.doctor.sun.dto.ApiDTO;
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

import retrofit2.Call;

/**
 * Created by kb on 13/12/2016.
 */

public class DoctorDetailActivity2 extends BaseFragmentActivity2 {

    private ActivityDoctorDetail2Binding binding;

    private ProfileModule api = Api.of(ProfileModule.class);
    private Doctor doctor = new Doctor();
    private int id;

    public static Intent makeIntent(Context context, Doctor data) {
        Intent i = new Intent(context, DoctorDetailActivity2.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail2);
        ToolModule api = Api.of(ToolModule.class);
        api.doctorInfo(getData().getId()).enqueue(new SimpleCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                doctor = response;
                binding.setData(doctor);
                if (response.getSpecialistCateg() == 1) {
                    binding.tvNet2.setText("咨询+取药");
                    binding.tvSurface2.setText("现场咨询+取药");
                }
                if (doctor.getIsOpen().isNetwork() == false) {
                    binding.radNet.setEnabled(false);
                    binding.radNet.setClickable(false);
                    binding.tvNet.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvNet2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }
                if (doctor.getIsOpen().isSurface() == false) {
                    binding.radSurface.setEnabled(false);
                    binding.radSurface.setClickable(false);
                    binding.tvSurface.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSurface2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSurfaceMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }
                if (doctor.getIsOpen().isSimple() == false) {
                    binding.radSimple.setEnabled(false);
                    binding.radSimple.setClickable(false);
                    binding.tvSimple.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSimple2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSimpleMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }
                if ("1".equals(doctor.getIsFav())) {
                    binding.tvConllect.setBackgroundResource(R.drawable.ic_collect);
                    binding.tvConllect.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    binding.tvConllect.setText("已收藏");
                } else {
                    binding.tvConllect.setBackgroundResource(R.drawable.ic_uncollect);
                    binding.tvConllect.setTextColor(getResources().getColor(R.color.text_color_gray));
                    binding.tvConllect.setText("未收藏");
                }
                binding.tvSimpleMoney.setText("￥" + doctor.getSecondMoney() + "/次");
                if (doctor.getSpecialistCateg() == 1) {
                    binding.radSimple.setVisibility(View.GONE);
                    binding.llVisible.setVisibility(View.VISIBLE);
                    binding.tvSimpleVisible.setVisibility(View.GONE);
                    binding.tvSimpleMoney.setText("");
                } else {
                    if (doctor.getIsOpen().isSimple()) {
                        binding.tvSimpleVisible.setVisibility(View.GONE);
                    }
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("unconllect");
        filter.addAction("conllect");
        registerReceiver(receiver, filter);
        postponeTransition();
        initWidget();
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


    }


    public void initWidget() {
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
                if (doctor.getIsOpen().isSurface() == false) {
                    binding.tvSurface.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSurface2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSurfaceMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }
                if (doctor.getIsOpen().isSimple() == false) {
                    binding.tvSimple.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSimple2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSimpleMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }
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
                if (doctor.getIsOpen().isNetwork() == false) {
                    binding.tvNet.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvNet2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }
                if (doctor.getIsOpen().isSurface() == false) {
                    binding.tvSurface.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSurface2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSurfaceMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }

            }
        });
        binding.radSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 4;
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
                if (doctor.getIsOpen().isNetwork() == false) {
                    binding.tvNet.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvNet2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }

                if (doctor.getIsOpen().isSimple() == false) {
                    binding.tvSimple.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSimple2.setTextColor(getResources().getColor(R.color.gray_ce));
                    binding.tvSimpleMoney.setTextColor(getResources().getColor(R.color.gray_ce));
                }
            }
        });
        binding.tvConllect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolModule api = Api.of(ToolModule.class);
                if ("1".equals(doctor.getIsFav())) {
                    api.unlikeDoctor(getData().getId()).enqueue(new SimpleCallback<String>() {
                        @Override
                        protected void handleResponse(String response) {
                            Intent toUpdate = new Intent();
                            toUpdate.setAction("unconllect");
                            sendBroadcast(toUpdate);
                            doctor.setIsFav("0");
                            Toast.makeText(DoctorDetailActivity2.this, "取消收藏医生", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    api.likeDoctor(getData().getId()).enqueue(new SimpleCallback<String>() {
                        @Override
                        protected void handleResponse(String response) {
                            Intent toUpdate = new Intent();
                            toUpdate.setAction("conllect");
                            sendBroadcast(toUpdate);
                            doctor.setIsFav("1");
                            Toast.makeText(DoctorDetailActivity2.this, "成功收藏医生", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        binding.flNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    Toast.makeText(DoctorDetailActivity2.this, "请选择就诊类型!", Toast.LENGTH_LONG).show();
                } else {
                    toOrderMessage(DoctorDetailActivity2.this);
                }

            }
        });

    }

    public void toOrderMessage(Context context) {
        Intent toOrder = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, doctor);
        toOrder.putExtra(Constants.REMARK, id);
        toOrder.putExtra(Constants.SIMPLE, doctor.getIsOpen().isSimple());
        toOrder.putExtra(Constants.SURFACE, doctor.getIsOpen().isSurface());
        toOrder.putExtra(Constants.NETWORK, doctor.getIsOpen().isNetwork());
        toOrder.putExtras(bundle);
        toOrder.putExtra(Constants.ADDRESS, binding.tvClinicAddress.getText().toString());
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
                binding.tvConllect.setBackgroundResource(R.drawable.ic_uncollect);
                binding.tvConllect.setTextColor(getResources().getColor(R.color.text_color_gray));
                binding.tvConllect.setText("未收藏");
            } else if (intent.getAction().equals("conllect")) {
                binding.tvConllect.setBackgroundResource(R.drawable.ic_collect);
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
