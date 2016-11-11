package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.activity.patient.DoctorDetailActivity;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.HashMap;

/**
 * Created by rick on 7/6/2016.
 * TODO: handler leak
 */
public class FurtherConsultationVM extends BaseItem {
    public static final String TAG = FurtherConsultationVM.class.getSimpleName();
    public static final String ANSWER_KEY = "option_id";
    public static final String ANSWER_VALUE = "reply_content";

    private long position = 0;
    private boolean hasAnswer = false;

    public Questions2 questions2;
    public ItemPickDate date;
    private Doctor doctor;

    private String questionContent;

    private boolean btnOneEnabled = false;
    private boolean btnTwoEnabled = false;
    private boolean btnThreeEnabled = false;

    private boolean btnOneChecked = false;
    private boolean btnTwoChecked = false;
    private boolean btnThreeChecked = false;
    private String questionId = "";

    public FurtherConsultationVM() {
        date = new ItemPickDate(0, "", 0);
    }

    public void chooseDoctor(Context context) {
        Intent intent = ContactActivity.makeIntent(context, Constants.DOCTOR_REQUEST_CODE);
        intent.putExtra(Constants.HANDLER, new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.DOCTOR_REQUEST_CODE: {
                        Doctor data = msg.getData().getParcelable(Constants.DATA);
                        setDoctor(data);
                        break;
                    }
                }
                return false;
            }
        })));
        context.startActivity(intent);
    }

    public void seeDoctorDetail(Context context, Doctor doctor) {
        Intent intent = DoctorDetailActivity.makeIntent(context, doctor, 1);
        context.startActivity(intent);
    }

    @Bindable
    public boolean isBtnTwoChecked() {
        return btnTwoChecked;
    }

    public void setBtnTwoChecked(boolean btnTwoChecked) {
        this.btnTwoChecked = btnTwoChecked;
        notifyPropertyChanged(BR.btnTwoChecked);
    }

    @Bindable
    public boolean isBtnThreeChecked() {
        return btnThreeChecked;
    }

    public void setBtnThreeChecked(boolean btnThreeChecked) {
        this.btnThreeChecked = btnThreeChecked;
        notifyPropertyChanged(BR.btnThreeChecked);
    }

    @Bindable
    public boolean isBtnOneChecked() {
        return btnOneChecked;
    }

    public void setBtnOneChecked(boolean btnOneChecked) {
        this.btnOneChecked = btnOneChecked;
        notifyPropertyChanged(BR.btnOneChecked);
    }

    @Bindable
    public boolean isBtnOneEnabled() {
        return btnOneEnabled;
    }

    public void setBtnOneEnabled(boolean btnOneEnabled) {
        this.btnOneEnabled = btnOneEnabled;
        notifyPropertyChanged(BR.btnOneEnabled);
    }

    @Bindable
    public boolean isBtnTwoEnabled() {
        return btnTwoEnabled;
    }

    public void setBtnTwoEnabled(boolean btnTwoEnabled) {
        this.btnTwoEnabled = btnTwoEnabled;
        notifyPropertyChanged(BR.btnTwoEnabled);
    }

    @Bindable
    public boolean isBtnThreeEnabled() {
        return btnThreeEnabled;
    }

    public void setBtnThreeEnabled(boolean btnThreeEnabled) {
        this.btnThreeEnabled = btnThreeEnabled;
        notifyPropertyChanged(BR.btnThreeEnabled);
    }

    @Bindable
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        notifyPropertyChanged(BR.doctor);
    }

    public void setDate(String string) {
        if (date != null) {
            date.setDate(string);
        }
    }

    public void pickDateImpl(Context context) {
        if (date != null) {
            date.pickDateImpl(context, btnOneChecked ? AppointmentType.PREMIUM : AppointmentType.STANDARD);
        }
    }


    @Bindable
    public boolean isHasAnswer() {
        return hasAnswer;
    }

    public void setHasAnswer(boolean hasAnswer) {
        this.hasAnswer = hasAnswer;
        notifyPropertyChanged(BR.hasAnswer);
    }

    @Bindable
    public long getPosition() {
        return position / QuestionsModel.PADDING + 1;
    }

    public void setPosition(long position) {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    @Bindable
    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
        notifyPropertyChanged(BR.questionContent);
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public boolean and(boolean b, boolean b2) {
        return b && b2;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_further_consultation;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_further_consultation;
    }

    @Override
    public long getCreated() {
        return -position;
    }

    @Override
    public String getKey() {
        return questions2.getKey();
    }

    @Override
    public int getSpan() {
        return 12;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (!isEnabled()) {
            return null;
        }
        HashMap<String, Object> result = new HashMap<>();

        String optionId = "";
        String replyContent = "";
        if (hasAnswer) {
            if (btnOneChecked) {
                optionId = questions2.getOptionID(0);
                replyContent = date.getDate();
                result.put(ANSWER_KEY, optionId);
                result.put(ANSWER_VALUE, replyContent);
                return result;
            }

            if (btnTwoChecked) {
                optionId = questions2.getOptionID(1);
                replyContent = date.getDate();
                result.put(ANSWER_KEY, optionId);
                result.put(ANSWER_VALUE, replyContent);
                return result;
            }
            if (btnThreeChecked) {
                optionId = questions2.getOptionID(2);
                if (doctor != null) {
                    replyContent = String.valueOf(doctor.getId());
                    result.put(ANSWER_KEY, optionId);
                    result.put(ANSWER_VALUE, replyContent);
                    return result;
                }
            }
        }


        return null;
    }
}
