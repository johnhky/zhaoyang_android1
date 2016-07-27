package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.event.SwitchTabEvent;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.FillForumFragment;
import com.doctor.sun.ui.fragment.ModifyForumFragment;
import com.doctor.sun.ui.handler.QCategoryHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.ConsultingDetailPagerAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.util.ShowCaseUtil;
import com.squareup.otto.Subscribe;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.extend.OnPageChangeAdapter;


/**
 * Created by rick on 12/16/15.
 */
public class ConsultingDetailActivity extends TabActivity
        implements QCategoryHandler.QCategoryCallback,
        Appointment.AppointmentId,
        Prescription.UrlToLoad {
    public static final int POSITION_ANSWER = 0;
    public static final int POSITION_SUGGESTION = 1;
    public static final int POSITION_SUGGESTION_READONLY = 2;
    private int position = 0;
    private HeaderViewModel header0;
    private HeaderViewModel header1;
    private boolean isReadOnly;

    public static Intent makeIntent(Context context, Appointment data, int position) {
        Intent i = new Intent(context, ConsultingDetailActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.POSITION, position);
        return i;
    }


    private Appointment getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public int getPosition() {
        return getIntent().getIntExtra(Constants.POSITION, POSITION_ANSWER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getPosition() == POSITION_SUGGESTION_READONLY) {
            isReadOnly = true;
        } else {
            isReadOnly = false;
        }
        super.onCreate(savedInstanceState);
        initListener();
        switchTab(new SwitchTabEvent(getPosition()));
        showCase();
    }

    private void showCase() {
        View childAt = binding.showcase;
        if (childAt != null) {
            if (Settings.isDoctor()) {
                ShowCaseUtil.showCase(childAt, "记录病历和给患者建议和调药", "diagnosisResult", 1, 0, true);
            } else {
                ShowCaseUtil.showCase(childAt, "您可以在这里看到医生的医嘱和用药建议", "diagnosisResult", 1, 0, true);
            }
        }
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new ConsultingDetailPagerAdapter(getSupportFragmentManager(), getData(), isReadOnly);
    }


    @Override
    protected HeaderViewModel createHeaderViewModel() {
        header0 = new HeaderViewModel(getHeaderView0());
        header1 = new HeaderViewModel(getHeaderView1());

        if (isUserPatient()) {
            //病人端
            header0.setRightTitle("");
            header1.setLeftTitle(getData().getHandler().getTitle());
        } else {
            //医生端
            if (!isReadOnly) {
                header0.setRightTitle("补充问卷");
                header0.setRightFirstTitle("删除问题");
                header1.setRightTitle("保存");
            }
        }

        if (getPosition() == 0) {
            position = 0;
            return header0;
        } else {
            position = 1;
            return header1;
        }
    }

    @NonNull
    private HeaderViewModel.HeaderView getHeaderView1() {
        return new HeaderViewModel.HeaderView() {
            @Override
            public void onBackClicked() {
                ConsultingDetailActivity.this.onBackClicked();
            }

            @Override
            public void onTitleClicked() {

            }

            @Override
            public void onMenuClicked() {
                DiagnosisFragment.getInstance(null).setDiagnosise();
            }

            @Override
            public void onFirstMenuClicked() {

            }
        };
    }

    @NonNull
    private HeaderViewModel.HeaderView getHeaderView0() {
        return new HeaderViewModel.HeaderView() {
            @Override
            public void onBackClicked() {
                ConsultingDetailActivity.this.onBackClicked();
            }

            @Override
            public void onTitleClicked() {

            }

            @Override
            public void onMenuClicked() {
                if (isUserPatient()) {
                    if (binding.getHeader().getRightTitle().equals("保存")) {
                        //保存
//                        Log.d("ConsultingDetailActivit", ModifyForumFragment.getInstance(getData().getAppointmentId()).toString());
                        ModifyForumFragment.getInstance(getData().getAppointmentId()).save();
                    }
                } else {
                    Intent intent = AssignQuestionActivity.makeIntent(ConsultingDetailActivity.this, getData());
                    startActivity(intent);
                }
            }

            @Override
            public void onFirstMenuClicked() {
                Answer.handler.toggleEditMode();
                FillForumFragment.getInstance(getData()).getAdapter().notifyDataSetChanged();
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Answer.handler.resetEditMode();
    }


    @Subscribe
    public void switchTab(SwitchTabEvent event) {
        if (event.getPosition() == -1) {
            return;
        }
        binding.vp.setCurrentItem(event.getPosition());
    }

    @Override
    public void onCategorySelect(QuestionCategory data) {
        if (isUserPatient()) {
            //病人端 具体问卷查看时 －－> 保存(有效)
            header0.setRightTitle("保存");
            ModifyForumFragment.getInstance(getData().getId()).loadQuestions(data);
        } else {
            FillForumFragment.getInstance(getData()).loadQuestions(data);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        switchTab(new SwitchTabEvent(isReadOnly ? 1 : -1));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (PickImageDialog.getRequestCode(requestCode)) {
            case Constants.DOCTOR_REQUEST_CODE:
            case Constants.PRESCRITION_REQUEST_CODE:
                DiagnosisFragment.getInstance(getData()).handlerResult(requestCode, resultCode, data);
                break;
            case Constants.PATIENT_PRESCRITION_REQUEST_CODE:
                ModifyForumFragment.getInstance(getData().getAppointmentId()).handleResult(requestCode, resultCode, data);
                break;
            case Constants.UPLOAD_REQUEST_CODE:
            case Constants.UPLOAD_REQUEST_CODE / 2:
                ModifyForumFragment.getInstance(getData().getAppointmentId()).handleImageResult(requestCode, resultCode, data);
                break;
        }
    }

    private void initListener() {
        getBinding().vp.addOnPageChangeListener(new OnPageChangeAdapter() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ConsultingDetailActivity.this.position = position;
                if (position == 0) {
                    getBinding().setHeader(header0);
                } else {
                    getBinding().setHeader(header1);
                }
            }
        });
    }

    /**
     * 病人 - true, 医生 - false
     *
     * @return
     */
    private boolean isUserPatient() {
        return Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE;
    }


    @Override
    public int getId() {
        return getData().getId();
    }

    @Override
    public String url() {
        return "diagnosis/last-drug?appointmentId=" + getId();
    }
}